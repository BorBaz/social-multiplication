function updateMultiplication() {

    const $formulario = $('#attempt-form');
    const $factorA = $('.multiplication-a');
    const $factorB = $('.multiplication-b');

    $.ajax({
        url: "/multiplications/random"
    }).then(function (data) {
        // Limpiamos el formulario
        $formulario.find("input[name='result-attempt']").val('');
        $formulario.find("input[name='user-alias']").val('');

        // Get un Multiplication random y lo introducimos en la capa de presentacion
        $factorA.empty().append(data.factorA);
        $factorB.empty().append(data.factorB);
    });
}

function updateStats(alias) {
    $.ajax({
        url: "http://localhost:8085/results?alias=" + alias,
    }).then(function (data) {
        $('#stats-body').empty();
        data.forEach(function (row) {
            $('#stats-body').append('<tr>' +
                '<td>' + row.id + '</td>' +
                '<td>' + row.multiplication.factorA + ' X ' + row.multiplication.factorB + '</td>' +
                '<td>' + row.resultAttempt + '</td>' +
                '<td>' + (row.correct === true ? 'SI' : 'NO') + '</td>' +
                '</tr>');
        })
    })
}

$(document).ready(function () {

    const $formulario = $('#attempt-form');
    const $factorA = $('.multiplication-a');
    const $factorB = $('.multiplication-b');

    // Actualizamos la multiplicación
    updateMultiplication();

    $formulario.submit(function (event) {
        // No hacemos submit del formulario de forma "normal"
        event.preventDefault();

        // Obtemos los valores de los elementos de la página
        let a = $factorA.text();
        let b = $factorB.text();
        let $form = $(this);
        let attempt = $form.find("input[name='result-attempt']").val();
        let userAlias = $form.find("input[name='user-alias']").val();

        // Preparamos la información conforme a como lo espera nuestra API
        let data = {
            user: {alias: userAlias},
            multiplication: {factorA: a, factorB: b},
            resultAttempt: attempt,
            correct: false
        };

        // enviamos a la API

        $.ajax({
            url: '/results',
            type: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: false,
            success: function (result) {
                if (result.correct) {
                    $('.result-message').empty().append("El resultado es correcto, ¡bien hecho!");
                } else {
                    $('.result-message').empty().append("El resultado es incorrecto, inútil.");
                }
            }
        })
        updateStats(userAlias);
        updateMultiplication();
    })
})