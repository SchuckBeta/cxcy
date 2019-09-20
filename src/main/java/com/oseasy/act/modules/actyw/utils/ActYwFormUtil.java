/**
 * .
 */

package com.oseasy.act.modules.actyw.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.oseasy.act.modules.actyw.tool.process.vo.FormTheme;
import com.oseasy.act.modules.actyw.tool.process.vo.FormType;
import com.oseasy.com.fileserver.common.utils.file.FileResManager;
import com.oseasy.util.common.utils.StringUtil;
import com.oseasy.util.common.utils.file.FileWrap;

public class ActYwFormUtil {
    public static List<FileWrap> listFile(String path, FormTheme theme, FormType type, boolean dirAndEditable) {
        List<FileWrap> fileWraps = new FileResManager().listFile(path, dirAndEditable);
        if((theme == null) && (type == null)){
            return fileWraps;
        }

        List<FileWrap> rtfwraps = Lists.newArrayList();
        if((theme == null) && (type != null)){
            for (FileWrap pfileWrap : fileWraps) {
                for (FileWrap fileWrap : pfileWrap.getChild()) {
                    if ((fileWrap.getFilename()).endsWith(type.getValue() + StringUtil.POSTFIX_JSP)) {
                        rtfwraps.add(fileWrap);
                    }
                }
            }
        }else if((theme != null) && (type == null)){
            for (FileWrap pfileWrap : fileWraps) {
                for (FileWrap fileWrap : pfileWrap.getChild()) {
                    if ((fileWrap.getFilename()).startsWith(theme.getKey())) {
                        rtfwraps.add(fileWrap);
                    }
                }
            }
        }else{
            for (FileWrap pfileWrap : fileWraps) {
                for (FileWrap fileWrap : pfileWrap.getChild()) {
                    if (((fileWrap.getFilename()).startsWith(theme.getKey())) && ((fileWrap.getFilename()).endsWith(type.getValue() + StringUtil.POSTFIX_JSP))) {
                        rtfwraps.add(fileWrap);
                    }
                }
            }
        }
        return rtfwraps;
    }
}
