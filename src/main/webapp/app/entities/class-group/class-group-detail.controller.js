(function() {
    'use strict';

    angular
        .module('batchappApp')
        .controller('ClassGroupDetailController', ClassGroupDetailController);

    ClassGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClassGroup', 'Student'];

    function ClassGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, ClassGroup, Student) {
        var vm = this;

        vm.classGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('batchappApp:classGroupUpdate', function(event, result) {
            vm.classGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
