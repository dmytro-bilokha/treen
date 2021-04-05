define(["require", "exports", 'knockout', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojlabel", "ojs/ojformlayout", "ojs/ojbutton"],
  function (require, exports, ko) {
    "use strict";

    class LoginModel {
      constructor() {
        var self = this;
        self.loginText = ko.observable();
        self.loginAction = (event) => {
          var rootViewModel = ko.dataFor(document.getElementById('globalBody'));
          rootViewModel.userLogin(self.loginText());
          return true;
        };
      }
    }
    return new LoginModel();
  });