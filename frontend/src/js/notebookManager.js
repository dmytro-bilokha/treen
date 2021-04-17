define([
  'knockout'
],
  function (ko, loginManager) {
    'use strict';

    class NotebookManager {

      init() {
        return $.ajax({
          url: '/api/notebook',
        }).done((data) => {
          this.notes(data);
        });
      }

      updateNote(note) {
        return $.ajax({
          url: '/api/notebook/note',
          type: 'PUT',
          contentType: 'application/json',
          data: JSON.stringify(note)
        }).done((data) => {
          this.notes(data);
        });
      }

      constructor() {
        this.notes = ko.observableArray();
        this.init = this.init.bind(this);
      }

    }

    return new NotebookManager();
  });