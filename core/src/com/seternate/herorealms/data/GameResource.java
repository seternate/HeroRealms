package com.seternate.herorealms.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class GameResource {

    public GameResource(){

        XmlReader xml = new XmlReader();
        Element root = xml.parse(Gdx.files.internal("data.xml"));
        System.out.println(root.getChild(0).toString());



    }

}
