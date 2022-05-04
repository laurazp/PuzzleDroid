package com.ultimapieza.puzzledroid;

import java.io.File;
import java.io.IOException;
//clase para esconder las fotos una vez seleccionadas
public class HideFile {
    public File path;

    public void setHiddenFile(File path) {
        this.path =path;

        try {
            // execute attrib command to set hide attribute
            Process p = Runtime.getRuntime().exec("attrib +H " + path.getPath());
            // for removing hide attribute
            //Process p = Runtime.getRuntime().exec("attrib -H " + file.getPath());
            p.waitFor();

        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}