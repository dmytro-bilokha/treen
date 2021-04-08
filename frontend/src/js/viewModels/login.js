define(["require", "exports", 'knockout', 'loginManager', "ojs/ojbootstrap", "ojs/ojknockout", "ojs/ojinputtext", "ojs/ojlabel", "ojs/ojformlayout", "ojs/ojbutton"],
  function (require, exports, ko, loginManager) {
    'use strict';

    class LoginModel {
      //TODO: implement spinner, block form during submit, handle errors with message
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
          loginManager.login(this.loginText(), self.passwordText());
          return true;
        };
      }
    }
    return new LoginModel();
  });