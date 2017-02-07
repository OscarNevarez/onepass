(function() {
    'use strict';

    angular
        .module('onepassApp')
        .controller('WebsiteDialogController', WebsiteDialogController);

    WebsiteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Website', 'Entry'];

    function WebsiteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Website, Entry) {
        var vm = this;

        vm.website = entity;
        vm.clear = clear;
        vm.save = save;
        vm.entries = Entry.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.website.id !== null) {
                Website.update(vm.website, onSaveSuccess, onSaveError);
            } else {
                Website.save(vm.website, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('onepassApp:websiteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
