(function() {
    'use strict';

    angular
        .module('batchappApp')
        .controller('ClassGroupDialogController', ClassGroupDialogController);

    ClassGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClassGroup', 'Student'];

    function ClassGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClassGroup, Student) {
        var vm = this;

        vm.classGroup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.students = Student.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.classGroup.id !== null) {
                ClassGroup.update(vm.classGroup, onSaveSuccess, onSaveError);
            } else {
                ClassGroup.save(vm.classGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('batchappApp:classGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
