define(["require", "exports", 'knockout', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojlabel", "ojs/ojformlayout", "ojs/ojbutton"],
  function (require, exports, ko) {
    "use strict";

    class LoginModel {
      //TODO: implement spinner, block form during submit, store credentials somewhere, handle errors with message
      constructor() {
        const self = this;
        self.loginText = ko.observable('');
        self.loginErrors = ko.observableArray();
        self.passwordText = ko.observable('');
        self.passwordErrors = ko.observableArray();
        self.loginAction = (event) => {
          self.loginErrors.removeAll();
          self.passwordErrors.removeAll();
          let nonValidForm = false;
          if (self.loginText() === '') {
            nonValidForm = true;
            self.loginErrors.push({ summary: 'Invalid login', detail: 'Login should not be empty', severity: 'error' });
          }
          if (self.passwordText() === '') {
            nonValidForm = true;
            self.passwordErrors.push({ summary: 'Invalid password', detail: 'Password should not be empty', severity: 'error' });
          }
          if (nonValidForm) {
            return true;
          }
          $.ajax({
            dataType: 'json',
            url: '/api/user',
            username: self.loginText(),
            password: self.passwordText()
          }).done((data, textStatus, jqXHR) => {
            console.log('Success');
            console.log(textStatus);
            console.log(data);
            const rootViewModel = ko.dataFor(document.getElementById('globalBody'));
            rootViewModel.userLogin(data.username);
          }).fail((jqXHR, textStatus, errorThrown) => {
            console.log("Fail :-(");
            console.log(textStatus);
            console.log(errorThrown);
          });
          return true;
        };
      }
    }
    return new LoginModel();
  });