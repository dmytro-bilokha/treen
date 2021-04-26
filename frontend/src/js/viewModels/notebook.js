define([
  'require',
  'exports',
  'knockout',
  'ojs/ojarraytreedataprovider',
  'notebookManager',
  'notificationManager',
  'loginManager',
  'ojs/ojknockout',
],
  function (require, exports, ko, ArrayTreeDataProvider, notebookManager, notificationManager, loginManager) {
    'use strict';

    class NotebookModel {

      constructor() {
        this.currentId = ko.observable();
        this.currentParentId = ko.observable();
        this.currentTitle = ko.observable('');
        this.currentLink = ko.observable('');
        this.currentDescription = ko.observable('');
        this.inputDisabled = ko.observable(false);
        this.titleErrors = ko.observableArray();
        this.linkErrors = ko.observableArray();

        this.openNoteDialog = (e, d, con) => {
          this.currentId(d.data.id);
          this.currentParentId(d.data.parentId);
          this.currentTitle(d.data.title);
          this.currentLink(d.data.link);
          this.currentDescription(d.data.description);
          document.getElementById('note-dialog').open();
          return true;
        };

        this.isFormValid = () => {
          return this.currentTitle() !== undefined && this.currentTitle() !== null && this.currentTitle() !== ''
            || this.currentLink() !== undefined && this.currentLink() !== null && this.currentLink() !== '';
        }

        this.submitAction = () => {
          this.titleErrors.removeAll();
          this.linkErrors.removeAll();
          notificationManager.removeAllNotificationsOfType('note');
          if (!this.isFormValid()) {
            this.titleErrors.push({ summary: 'Invalid data', detail: 'Either title of link should not be empty', severity: 'error' });
            this.linkErrors.push({ summary: 'Invalid data', detail: 'Either title or link should not be empty', severity: 'error' });
            return true;
          }
          this.inputDisabled(true);
          const noteData = {
            id: this.currentId(),
            parentId: this.currentParentId(),
            title: this.currentTitle() === undefined ? null : this.currentTitle(),
            link: this.currentLink() === undefined ? null : this.currentLink(),
            description: this.currentDescription() === undefined ? null : this.currentDescription(),
          };
          const notebookAction = noteData.id ? notebookManager.updateNote(noteData) : notebookManager.createNote(noteData);
          notebookAction
            .fail(this.handleServerError)
            .done(() => {
              document.getElementById('note-dialog').close();
            }).always(() => {
              this.inputDisabled(false);
            });
          return true;
        };

        this.closeNoteDialog = () => {
          document.getElementById('note-dialog').close();
          return true;
        };

        this.createFirstNote = () => {
          this.openNewNoteDialog(null);
        };

        this.menuBeforeOpen = (event) => {
          const target = event.detail.originalEvent.target;
          const treeView = document.getElementById("treeview");
          const context = treeView.getContextByNode(target);
          this.currentMenuKey = context ? context.key : treeView.currentItem;
        };

        this.openNewNoteDialog = (parentId) => {
          this.currentId(null);
          this.currentParentId(parentId);
          this.currentTitle(null);
          this.currentLink(null);
          this.currentDescription(null);
          document.getElementById('note-dialog').open();
        };

        this.menuAction = (event) => {
          const actionCode = event.target.value;
          this.notesProvider
            .fetchByKeys({ keys: new Set([this.currentMenuKey]) })
            .then((e) => {
              const actionTarget = e.results.get(this.currentMenuKey);
              if (actionTarget) {
                switch (actionCode) {
                  case 'edit':
                    this.openNoteDialog(null, actionTarget);
                    break;

                  case 'addChild':
                    this.openNewNoteDialog(actionTarget.data.id);
                    break;

                  case 'addSibling':
                    this.openNewNoteDialog(actionTarget.data.parentId);
                    break;

                  case 'delete':
                    notebookManager.deleteNote(actionTarget.data)
                      .fail(this.handleServerError);
                    break;
                }
              }
            });
        };

        this.handleServerError = (jqXHR, textStatus, errorThrown) => {
          let serverMessage = null;
          if (jqXHR.responseJSON) {
            serverMessage = jqXHR.responseJSON.message;
          }
          if (errorThrown === 'Unauthorized') {
            loginManager.registerFailedAuthorization();
            notificationManager.addNotification({
              severity: 'error',
              summary: 'Authorization error',
              detail: serverMessage ? serverMessage : 'You need to login to access the notebook',
              type: 'login'
            });
          } else {
            notificationManager.addNotification({
              severity: 'error',
              summary: 'Operation has failed',
              detail: serverMessage ? serverMessage : `${textStatus} - ${errorThrown}`,
              type: 'note'
            });
          }
        };

        this.connected = () => {
          notebookManager.init()
            .fail(this.handleServerError);
        }

        this.notesProvider = new ArrayTreeDataProvider(notebookManager.notesTree);
      }

    }

    return new NotebookModel();
  });