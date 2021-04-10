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
        this.notesProvider = new ArrayTreeDataProvider(
          [
            {
              "title":"News",
              "id":"news"
            },
            {
              "title":"Blogs",
              "id":"blogs",
              "children":[
                {
                  "title":"Today",
                  "id":"today"
                },
                {
                  "title":"Yesterday",
                  "id":"yesterday"
                },
                {
                  "title":"Archive",
                  "id":"archive"
                }
              ]
            }
          ]);
      }

    }

    return new NotebookModel();
  });