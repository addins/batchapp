(function() {
    'use strict';

    angular
        .module('batchappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('class-group', {
            parent: 'entity',
            url: '/class-group?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'batchappApp.classGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-group/class-groups.html',
                    controller: 'ClassGroupController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classGroup');
                    $translatePartialLoader.addPart('schoolType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('class-group-detail', {
            parent: 'class-group',
            url: '/class-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'batchappApp.classGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/class-group/class-group-detail.html',
                    controller: 'ClassGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('classGroup');
                    $translatePartialLoader.addPart('schoolType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClassGroup', function($stateParams, ClassGroup) {
                    return ClassGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'class-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('class-group-detail.edit', {
            parent: 'class-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-group/class-group-dialog.html',
                    controller: 'ClassGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassGroup', function(ClassGroup) {
                            return ClassGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-group.new', {
            parent: 'class-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-group/class-group-dialog.html',
                    controller: 'ClassGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                classCode: null,
                                schoolType: null,
                                classLevel: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('class-group', null, { reload: 'class-group' });
                }, function() {
                    $state.go('class-group');
                });
            }]
        })
        .state('class-group.edit', {
            parent: 'class-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-group/class-group-dialog.html',
                    controller: 'ClassGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClassGroup', function(ClassGroup) {
                            return ClassGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-group', null, { reload: 'class-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('class-group.delete', {
            parent: 'class-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/class-group/class-group-delete-dialog.html',
                    controller: 'ClassGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClassGroup', function(ClassGroup) {
                            return ClassGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('class-group', null, { reload: 'class-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
