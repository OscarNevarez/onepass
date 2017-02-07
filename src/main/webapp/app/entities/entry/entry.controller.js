(function() {
    'use strict';

    angular
        .module('onepassApp')
        .controller('EntryController', EntryController);

    EntryController.$inject = ['$scope', '$state', 'Entry'];

    function EntryController ($scope, $state, Entry) {
        var vm = this;

        vm.entries = [];

        loadAll();

        function loadAll() {
            Entry.query(function(result) {
                vm.entries = result;
                vm.searchQuery = null;
            });
        }
    }
})();
