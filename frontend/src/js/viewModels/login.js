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
          $.ajax({
            dataType: 'json',
            url: '/api/notebook',
            username: 'tomee',
            password: 'tomee',
            success: () => {console.log("Success!");}
          }).done(() => {console.log("Success 2");})
          .fail((jqXHR, textStatus, errorThrown ) => {console.log("Fail :-(");
        console.log(textStatus);
        console.log(errorThrown);
        });
          return true;
        };
      }
    }
    return new LoginModel();
  });