(function() {
    'use strict';

    angular
        .module('onepassApp')
        .controller('EntryDetailController', EntryDetailController);

    EntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Entry'];

    function EntryDetailController($scope, $rootScope, $stateParams, previousState, entity, Entry) {
        var vm = this;

        vm.entry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('onepassApp:entryUpdate', function(event, result) {
            vm.entry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
