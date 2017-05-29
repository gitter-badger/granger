var app = angular.module('grangerApp', []);

app.controller('MainController', function($q, $http) {

    var ctrl = this;

    ctrl.allPatients = [];

    ctrl.selectedPatient = null;

    ctrl.selectedTooth = null;

    function getAllPatients() {
        return $http({
            method: "get",
            url: '/api',
        }).then(function(resp) {
            return resp.data;
        });
    }

    ctrl.selectPatient = function(patient) {
      ctrl.selectedPatient = patient;
    };

    ctrl.deselectPatient = function() {
        ctrl.selectedPatient = null;
    };

    ctrl.deselectTooth = function() {
        ctrl.selectedTooth = null;
    };

    ctrl.selectTooth = function(tooth) {
        ctrl.selectedTooth = tooth;
    };

    ctrl.firstTeethRow = function(criteria) {
        return criteria.number <= 28;
    };

    ctrl.secondTeethRow = function(criteria) {
        return criteria.number > 28;
    };

    getAllPatients().then(function(patients) {
       ctrl.allPatients = patients;
    });
});