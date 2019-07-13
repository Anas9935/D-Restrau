package com.example.drestrau.Objects;

public class menuObjForFood {
    private final menuObject object;
    private final String rid;
    public menuObjForFood(menuObject obj,String id){
        object=obj;
        rid=id;
    }

    public String getRid() {
        return rid;
    }

    public menuObject getObject() {
        return object;
    }
}
