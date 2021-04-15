define([
  'knockout'
],
  function (ko) {
    'use strict';

    class NotebookManager {

      init() {
        $.ajax({
          url: '/api/notebook',
        }).done((data) => {
          this.notes(data);
        }).fail(() => {
          console.log('Failed to get the notebook');
        });
      }

      constructor() {
        this.notes = ko.observableArray();
        this.init = this.init.bind(this);
      }

    }

    return new NotebookManager();
  });