define([
  'require',
  'exports',
  'knockout',
  'loginManager',
  'ojs/ojbootstrap',
  'ojs/ojknockout',
  'ojs/ojinputtext',
  'ojs/ojlabel',
  'ojs/ojformlayout',
  'ojs/ojbutton'
],
  function (require, exports, ko, loginManager) {
    'use strict';

    class LoginModel {

      //TODO: implement spinner, block form during submit, handle errors with message
      constructor() {
        this.loginText = ko.observable('');
        this.loginErrors = ko.observableArray();
        this.passwordText = ko.observable('');
        this.passwordErrors = ko.observableArray();
        this.loginAction = () => {
          this.loginErrors.removeAll();
          this.passwordErrors.removeAll();
          let nonValidForm = false;
          if (this.loginText() === '') {
            nonValidForm = true;
            this.loginErrors.push({ summary: 'Invalid login', detail: 'Login should not be empty', severity: 'error' });
          }
          if (this.passwordText() === '') {
            nonValidForm = true;
            this.passwordErrors.push({ summary: 'Invalid password', detail: 'Password should not be empty', severity: 'error' });
          }
          if (nonValidForm) {
            return true;
          }
          loginManager
            .login(this.loginText(), this.passwordText())
            .done((data, textStatus, jqXHR) => {
              console.log('Success');
              console.log(textStatus);
              console.log(data);
            }).fail((jqXHR, textStatus, errorThrown) => {
              console.log('Fail :-(');
              console.log(textStatus);
              console.log(errorThrown);
            });
          return true;
        };
      }

    }

    return new LoginModel();
  });