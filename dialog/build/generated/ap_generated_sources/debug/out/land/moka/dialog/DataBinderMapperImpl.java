package land.moka.dialog;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import land.moka.dialog.databinding.LayoutBaseDialogBindingImpl;
import land.moka.dialog.databinding.LayoutLoadingDialogBindingImpl;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_LAYOUTBASEDIALOG = 1;

  private static final int LAYOUT_LAYOUTLOADINGDIALOG = 2;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(2);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(land.moka.dialog.R.layout.layout_base_dialog, LAYOUT_LAYOUTBASEDIALOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(land.moka.dialog.R.layout.layout_loading_dialog, LAYOUT_LAYOUTLOADINGDIALOG);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_LAYOUTBASEDIALOG: {
          if ("layout/layout_base_dialog_0".equals(tag)) {
            return new LayoutBaseDialogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_base_dialog is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTLOADINGDIALOG: {
          if ("layout/layout_loading_dialog_0".equals(tag)) {
            return new LayoutLoadingDialogBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_loading_dialog is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(2);

    static {
      sKeys.put("layout/layout_base_dialog_0", land.moka.dialog.R.layout.layout_base_dialog);
      sKeys.put("layout/layout_loading_dialog_0", land.moka.dialog.R.layout.layout_loading_dialog);
    }
  }
}
