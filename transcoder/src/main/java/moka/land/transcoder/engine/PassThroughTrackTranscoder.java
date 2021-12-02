/*
 * Copyright (C) 2014 Yuya Tanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moka.land.transcoder.engine;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PassThroughTrackTranscoder implements TrackTranscoder {
    private final MediaExtractor mExtractor;
    private final int mTrackIndex;
    private final QueuedMuxer mMuxer;
    private final QueuedMuxer.SampleType mSampleType;
    private final MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
    private int mBufferSize;
    private ByteBuffer mBuffer;
    private boolean mIsEOS;
    private MediaFormat mActualOutputFormat;
    private long mWrittenPresentationTimeUs;

    public PassThroughTrackTranscoder(MediaExtractor extractor, int trackIndex,
                                      QueuedMuxer muxer, QueuedMuxer.SampleType sampleType) {
        mExtractor = extractor;
        mTrackIndex = trackIndex;
        mMuxer = muxer;
        mSampleType = sampleType;

        mActualOutputFormat = mExtractor.getTrackFormat(mTrackIndex);
        mMuxer.setOutputFormat(mSampleType, mActualOutputFormat);

        mBufferSize = mActualOutputFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
        mBuffer = ByteBuffer.allocateDirect(mBufferSize).order(ByteOrder.nativeOrder());
    }

    @Override
    public void setup() {
    }

    @Override
    public MediaFormat getDeterminedFormat() {
        return mActualOutputFormat;
    }

    @SuppressLint("Assert")
    @Override
    public boolean stepPipeline() {
        if (mIsEOS) return false;
        int trackIndex = mExtractor.getSampleTrackIndex();
        if (trackIndex < 0) {
            mBuffer.clear();
            mBufferInfo.set(0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
            mMuxer.writeSampleData(mSampleType, mBuffer, mBufferInfo);
            mIsEOS = true;
            return true;
        }
        if (trackIndex != mTrackIndex) return false;

        mBuffer.clear();
        int sampleSize = mExtractor.readSampleData(mBuffer, 0);
        assert sampleSize <= mBufferSize;
        boolean isKeyFrame = (mExtractor.getSampleFlags() & MediaExtractor.SAMPLE_FLAG_SYNC) != 0;
        int flags = isKeyFrame ? MediaCodec.BUFFER_FLAG_SYNC_FRAME : 0;
        mBufferInfo.set(0, sampleSize, mExtractor.getSampleTime(), flags);
        mMuxer.writeSampleData(mSampleType, mBuffer, mBufferInfo);
        mWrittenPresentationTimeUs = mBufferInfo.presentationTimeUs;

        mExtractor.advance();
        return true;
    }

    @Override
    public long getWrittenPresentationTimeUs() {
        return mWrittenPresentationTimeUs;
    }

    @Override
    public boolean isFinished() {
        return mIsEOS;
    }

    @Override
    public void release() {
    }

    private MediaFormat makeAACCodecSpecificData(int audioProfile, int sampleRate, int channelConfig) {
        MediaFormat format = new MediaFormat();
        format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm"); // AAC의 Decoder type
        format.setInteger(MediaFormat.KEY_SAMPLE_RATE, sampleRate); // sample rate 정의
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, channelConfig); // channel 정의

        int samplingFreq[] = { // Android 참고 코드상 아래와 같은 samplerate를 지원
                96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050,
                16000, 12000, 11025, 8000
        };

        // Search the Sampling Frequencies
        // 아래 코드를 통해 0~11 에 맞는 값을 가져와야 합니다.
        // 일반적으로 44100을 사용하고 있으며, 여기에서는 4번에 해당됩니다.
        int sampleIndex = -1;
        for (int i = 0; i < samplingFreq.length; ++i) {
            if (samplingFreq[i] == sampleRate) {
                Log.d("TAG", "kSamplingFreq " + samplingFreq[i] + " i : " + i);
                sampleIndex = i;
            }
        }

        if (sampleIndex == -1) {
            return null;
        }

        /* 디코딩에 필요한 csd-0의 byte를 생성합니다. 이 부분은 Android 4.4.2의 Full source를 참고하여 작성
         * csd-0에서 필요한 byte는 2 byte 입니다. 2byte에 필요한 정보는 audio Profile 정보와
         * sample index, channelConfig 정보가 됩니다.
         */
        ByteBuffer csd = ByteBuffer.allocate(2);
        // 첫 1 byte에는 Audio Profile에 3 bit shift 처리합니다. 그리고 sample index를 1bit shift 합니다.
        csd.put((byte) ((audioProfile << 3) | (sampleIndex >> 1)));

        csd.position(1);
        // 다음 1 byte에는 sample index를 7bit shift 하고, channel 수를 3bit shift 처리합니다.
        csd.put((byte) ((byte) ((sampleIndex << 7) & 0x80) | (channelConfig << 3)));
        csd.flip();
        // MediaCodec에서 필요하는 MediaFormat에 방금 생성한 "csd-0"을 저장합니다.
        format.setByteBuffer("csd-0", csd); // add csd-0

        return format;
    }

}
