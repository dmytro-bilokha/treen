define([
  'knockout'
],
  function (ko) {
    'use strict';

    class LoginManager {

      login(login, password) {
        return $.ajax({
          url: '/auth/login',
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({ login: login, password: password })
        }).done(() => {
          this.userLogin(login);
        }).fail(() => {
          this.userLogin('');
        });
      }

      logout() {
        return $.ajax({
          url: '/auth/logout',
        }).done(() => {
          this.userLogin('');
        }).fail((jqXHR, textStatus, errorThrown) => {
          console.log('Fail :-(');
          console.log(textStatus);
          console.log(errorThrown);
        });
      }

      constructor() {
        this.userLogin = ko.observable('');
        this.login = this.login.bind(this);
        this.logout = this.logout.bind(this);
      }

    }

    return new LoginManager();
  });