define([
  'require',
  'exports',
  'knockout',
  'ojs/ojarraytreedataprovider',
  'notebookManager',
  'ojs/ojknockout',
],
  function (require, exports, ko, ArrayTreeDataProvider, notebookManager) {
    'use strict';

    class NotebookModel {

      constructor() {
        this.headMessage = ko.observable(' WTF?');
        this.currentDescription = ko.observable('');
        this.itemClick = (e, d, con) => {
          if (d.data.description !== undefined && d.data.description !== null) {
            this.currentDescription(d.data.description);
            document.getElementById('description-dialog').open();
          }
          return true;
        };
        this.connected = () => {
          notebookManager.init();
        }
        this.notesProvider = new ArrayTreeDataProvider(notebookManager.notes);
      }

    }

    return new NotebookModel();
  });