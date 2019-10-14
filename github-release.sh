#!/usr/bin/env bash

# https://developer.github.com/v3/repos/releases/#create-a-release

repo=https://github.com/moka-a/MokaAStd
tag=0.1.0
name=wip
text=wip
# token=$GH_TOKEN


generate_post_data()
{
  cat <<EOF
{
  "tag_name": "$tag",
  "name": "$name",
  "body": "$text",
  "draft": false,
  "prerelease": false
}
EOF
}

# curl --data "$(generate_post_data)" "https://api.github.com/repos/$repo/releases?access_token=$token"
curl --data "$(generate_post_data)" "https://api.github.com/repos/$repo/releases"
