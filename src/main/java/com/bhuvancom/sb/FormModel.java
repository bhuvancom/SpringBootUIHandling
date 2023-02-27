package com.bhuvancom.sb;

public class FormModel {
    private String userName;
    private String route;
    private String userPass;

    private String actionOfTheForm;


    public FormModel() {
    }

    public FormModel(String userName, String route, String userPass, String actionOfTheForm) {
        this.userName = userName;
        this.route = route;
        this.userPass = userPass;
        this.actionOfTheForm = actionOfTheForm;
    }


    public String getActionOfTheForm() {
        return actionOfTheForm;
    }

    public void setActionOfTheForm(String actionOfTheForm) {
        this.actionOfTheForm = actionOfTheForm;
    }

    @Override
    public String toString() {
        return "FormModel{" +
                "userName='" + userName + '\'' +
                ", route='" + route + '\'' +
                ", userPass='" + userPass + '\'' +
                ", actionOfTheForm='" + actionOfTheForm + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
