(function() {
    'use strict';

    angular
        .module('onepassApp')
        .controller('WebsiteDetailController', WebsiteDetailController);

    WebsiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Website', 'Entry'];

    function WebsiteDetailController($scope, $rootScope, $stateParams, previousState, entity, Website, Entry) {
        var vm = this;

        vm.website = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('onepassApp:websiteUpdate', function(event, result) {
            vm.website = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
