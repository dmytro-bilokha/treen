define([
  'require',
  'exports',
  'knockout',
  'ojs/ojarraytreedataprovider',
  'ojs/ojknockout',
],
  function (require, exports, ko, ArrayTreeDataProvider) {
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
        this.notesProvider = new ArrayTreeDataProvider(
          [
            {
              "title":"News",
              "id":"news",
              "link":"https://www.linux.org.ru",
              "description": "This is the dialog content.\
               User can change dialog resize behavior, cancel behavior and drag behavior by setting attributes.\
                Default attribute value depends on the theme."
            },
            {
              "title":"Blogs",
              "id":"blogs",
              "link":"https://www.linux.org.ru",
              "children":[
                {
                  "title":"Today",
                  "id":"today"
                },
                {
                  "title":"Yesterday",
              "link":"https://www.linux.org.ru",
                  "id":"yesterday"
                },
                {
                  "title":"Archive",
              "link":"https://www.linux.org.ru",
                  "id":"archive"
                }
              ]
            }
          ]);
      }

    }

    return new NotebookModel();
  });