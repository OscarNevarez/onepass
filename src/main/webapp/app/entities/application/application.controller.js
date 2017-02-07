(function() {
    'use strict';

    angular
        .module('onepassApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', '$state', 'Application'];

    function ApplicationController ($scope, $state, Application) {
        var vm = this;

        vm.applications = [];

        loadAll();

        function loadAll() {
            Application.query(function(result) {
                vm.applications = result;
                vm.searchQuery = null;
            });
        }
    }
})();
