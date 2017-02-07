(function() {
    'use strict';

    angular
        .module('onepassApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Application', 'Entry'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, entity, Application, Entry) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('onepassApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
