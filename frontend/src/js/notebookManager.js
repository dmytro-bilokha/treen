define([
  'knockout'
],
  function (ko, loginManager) {
    'use strict';

    class NotebookManager {

      buildNestedObservable(data) {
        return ko.observableArray(data.map((node) => {
          const newNode = {
            id: node.id,
            title: node.title,
            link: node.link,
            description: node.description,
            version: node.version
          };
          if (node.children) {
            newNode.children = this.buildNestedObservable(node.children);
          }
          return newNode;
        }));
      }

      findNodePlace(topObservableArray, id) {
        const arrayData = topObservableArray();
        for (let i = 0; i < arrayData.length; i += 1) {
          if (arrayData[i].id == id) {
            const target = {
              enclosingArray: topObservableArray,
              index: i,
            };
            return target;
          }
          else if (arrayData[i].children) {
            const target = this.findNodePlace(arrayData[i].children, id);
            if (target) {
              return target;
            }
          }
        }
      }

      init() {
        return $.ajax({
          url: '/api/notebook',
        }).done((data) => {
          this.notesTree(this.buildNestedObservable(data)());
        });
      }

      updateNote(note) {
        return $.ajax({
          url: '/api/notebook/note',
          type: 'PUT',
          contentType: 'application/json',
          data: JSON.stringify(note)
        }).done(() => {
          const targetLocation = this.findNodePlace(this.notesTree, note.id);
          const targetNode = targetLocation.enclosingArray()[targetLocation.index];
          const updatedNode = {
            id: targetNode.id,
            title: note.title,
            link: note.link,
            description: note.description,
            version: note.version + 1
          };
          if (targetNode.children) {
            updatedNode.children = ko.observableArray(targetNode.children());
          }
          //targetLocation.enclosingArray.splice(targetLocation.index, 1, updatedNode);
          targetLocation.enclosingArray.replace(targetNode, updatedNode);
        });
      }

      constructor() {
        this.notesTree = ko.observableArray();
        this.init = this.init.bind(this);
      }

    }

    return new NotebookManager();
  });