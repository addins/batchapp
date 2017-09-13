(function() {
    'use strict';

    angular
        .module('batchappApp')
        .controller('ClassGroupDeleteController',ClassGroupDeleteController);

    ClassGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClassGroup'];

    function ClassGroupDeleteController($uibModalInstance, entity, ClassGroup) {
        var vm = this;

        vm.classGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClassGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
