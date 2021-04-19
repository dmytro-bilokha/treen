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
        this.headMessage = ko.observable(' WTF?');
        this.currentId = ko.observable();
        this.currentTitle = ko.observable('');
        this.currentLink = ko.observable('');
        this.currentDescription = ko.observable('');
        this.currentVersion = ko.observable();
        this.inputDisabled = ko.observable(false);
        this.titleErrors = ko.observableArray();
        this.linkErrors = ko.observableArray();

        this.openNoteDialog = (e, d, con) => {
          this.currentId(d.data.id);
          this.currentTitle(d.data.title);
          this.currentLink(d.data.link);
          this.currentDescription(d.data.description);
          this.currentVersion(d.data.version);
          document.getElementById('note-dialog').open();
          return true;
        };

        this.isFormValid = () => {
          return this.currentTitle() !== undefined && this.currentTitle() !== null && this.currentTitle() !== ''
            ||  this.currentLink() !== undefined && this.currentLink() !== null && this.currentLink() !== '';
        }

        this.submitAction = () => {
          this.titleErrors.removeAll();
          this.linkErrors.removeAll();
          notificationManager.removeAllNotificationsOfType('edit-note');
          if (!this.isFormValid()) {
            this.titleErrors.push({ summary: 'Invalid data', detail: 'Either title of link should not be empty', severity: 'error' });
            this.linkErrors.push({ summary: 'Invalid data', detail: 'Either title or link should not be empty', severity: 'error' });
            return true;
          }
          this.inputDisabled(true);
          notebookManager.updateNote({
            id: this.currentId(),
            title: this.currentTitle() === undefined ? null : this.currentTitle(),
            link: this.currentLink() === undefined ? null : this.currentLink(),
            description: this.currentDescription() === undefined ? null : this.currentDescription(),
            version: this.currentVersion()
          }).fail((jqXHR, textStatus, errorThrown) => {
            if (errorThrown === 'Unathorized') {
              loginManager.registerFailedAuthorization();
              notificationManager.addNotification({
                severity: 'error',
                summary: 'Authorization error',
                detail: 'You need to login to access the notebook',
                type: 'login'
              });
            } else {
              notificationManager.addNotification({
                severity: 'error',
                summary: 'Operation has failed',
                detail: `${textStatus} - ${errorThrown}`,
                type: 'edit-note'
              });
            }
          }).done(() => {
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

        this.menuBeforeOpen = (event) => {
          const target = event.detail.originalEvent.target;
          const treeView = document.getElementById("treeview");
          const context = treeView.getContextByNode(target);
          this.currentMenuKey = context ? context.key : treeView.currentItem;
        };

      this.menuAction = (event) => {
          const text = event.target.textContent;
          this.notesProvider
              .fetchByKeys({ keys: new Set([this.currentMenuKey]) })
              .then((e) => {
              if (e.results.get(this.currentMenuKey)) {
                  console.log(text + " from " + e.results.get(this.currentMenuKey).data.title);
              }
          });
      };

        this.connected = () => {
          notebookManager.init()
            .fail((jqXHR, textStatus, errorThrown) => {
              if (errorThrown === 'Unathorized') {
                loginManager.registerFailedAuthorization();
                notificationManager.addNotification({
                  severity: 'error',
                  summary: 'Authorization error',
                  detail: 'You need to login to access the notebook',
                  type: 'login'
                });
              } else {
                notificationManager.addNotification({
                  severity: 'error',
                  summary: 'Operation has failed',
                  detail: `${textStatus} - ${errorThrown}`,
                  type: 'note'
                });
              }
            });
        }

        this.notesProvider = new ArrayTreeDataProvider(notebookManager.notesTree);
      }

    }

    return new NotebookModel();
  });