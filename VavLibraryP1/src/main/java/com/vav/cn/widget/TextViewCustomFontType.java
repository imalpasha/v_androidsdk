package com.vav.cn.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vav.cn.config.Config;
import com.vav.cn.util.GeneralUtil;

/**
 * Created by Handrata Samsul on 11/18/2015.
 */
public class TextViewCustomFontType extends TextView {

    public TextViewCustomFontType(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    private void processAttributeSet(AttributeSet attrs) {
        //This method reads the parameters given in the xml file and sets the properties according to it
        String strTypeFace = attrs.getAttributeValue(null, "customTF");
        String strCustomStyle = attrs.getAttributeValue(null, "customStyle");
        if (strTypeFace == null) {
            strTypeFace = Config.FONT_PRIMARY;
        }
        Typeface tFHelveticaLight = GeneralUtil.getFontTypeFace(getContext(), strTypeFace);
        if (strCustomStyle == null) {
            strCustomStyle = Config.FONT_STYLE_PRIMARY;
        }

        if (strCustomStyle.equals(Config.FONT_STYLE_BOLD)) {
            this.setTypeface(tFHelveticaLight, Typeface.BOLD);
        } else {
            this.setTypeface(tFHelveticaLight);
        }
    }
}
