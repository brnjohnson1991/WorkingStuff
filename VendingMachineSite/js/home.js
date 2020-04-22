$(document).ready(function () {
    displayItems();
});

function displayItems(id=-1) {
    $.ajax({
        type: "GET",
        url: "http://tsg-vending.herokuapp.com/items",
        success: function (itemArray) {
            $('.inventory').empty();
            $.each(itemArray, function (index, item) {
                var preparedHtml;
                if(parseInt(item.id) == id) {
                preparedHtml =
                    '<button class="col-sm-3 m-2 selected"' +
                    'id="itemButton' + item.id + '" ' +
                    'onclick = selectItem(' + item.id + ')>' +
                    '<p class="text-left id">' + item.id + '</p>' +
                    '<p class="text-center name">' + item.name + '</p>' +
                    '<p class="text-center"> $' + item.price.toFixed(2) + '</p>' +
                    '<p class="text-center"> Quantity left: ' + item.quantity + '</p>' +
                    '</button>';
                } else {
                    preparedHtml =
                    '<button class="col-sm-3 m-2"' +
                    'id="itemButton' + item.id + '" ' +
                    'onclick = selectItem(' + item.id + ')>' +
                    '<p class="text-left id">' + item.id + '</p>' +
                    '<p class="text-center name">' + item.name + '</p>' +
                    '<p class="text-center"> $' + item.price.toFixed(2) + '</p>' +
                    '<p class="text-center"> Quantity left: ' + item.quantity + '</p>' +
                    '</button>';
                }
                $('.inventory').append(preparedHtml);
            });
        },
        error: function () {
            $('#messages').text('Inventory failed to load.');
            resetMessagesStatus();
            $('#messages').addClass('danger');
        }
    });
}

function makePurchase() {
    var selectedItem = $('.inventory').find('.selected');

    if (selectedItem.length == 0) {
        $('#messages').text('Select an item first.');
        resetMessagesStatus();
        $('#messages').addClass('warning');
    }
    var id = selectedItem.find('.id').text().toString();
    var amount = $('#money').text().substr(1);

    $.ajax({
        type: "POST",
        data: "",
        url: "http://tsg-vending.herokuapp.com/money/" + amount + "/item/" + id,
        contentType: "application/json;charset=UTF-8",
        success: function (change) {
            $('#messages').text('THANK YOU!!!');
            resetMessagesStatus();
            $('#messages').addClass('success');
            var total = change.quarters * 0.25 +
                change.dimes * 0.10 +
                change.nickels * 0.05 +
				change.pennies * 0.01;
            var out = '$' + total.toFixed(2);
            $('#money').text(out);
            displayItems(id);
        },
        statusCode: {
            422: function (error) {
                var message = JSON.parse(error.responseText).message;
                $('#messages').text(message);
                resetMessagesStatus();
                $('#messages').addClass('warning');
            },
            500: function(error) {
                var message = "Server error";
                $('#messages').text(message);
                resetMessagesStatus();
                $('#messages').addClass('warning')
            }
        }
    });
}

function selectItem(id) {
    var button = $('#itemButton' + id);
    $('.selected').removeClass('selected');
    button.addClass('selected');
    $('.itemDisplay').text(button.find('.id').text());
}

function addChange(n) {
    var total = $('#money').text();
    total = parseFloat(total.substr(1)) + n;

    $('#money').text('$' + total.toFixed(2));
    $('#change').html('&emsp;');
}

function coinReturn() {
    var total = $('#money').text();
    total = parseFloat(total.substr(1));

    var q = 0;
    var d = 0;
    var n = 0;

    q = total / 0.25;
    q = Math.floor(q);
    total = total - q * 0.25;
    d = total / 0.10;
    d = Math.floor(d);
    total = total - d * 0.10;
    if (total > 0) {
        n = 1;
    }


    var out = '';
    if (q == 1) {
        out += '1 Quarter<br id="trailingQuarterBr" />';
    } else if (q > 0) {
        out += q + ' Quarters<br id="trailingQuarterBr" />';
    }

    if(d == 0) {
        $('#trailingQuarterBr').remove();
    } else if (d == 1) {
        out += '1 Dime<br id="trailingDimeBr" />';
    } else if (d > 0) {
        out += d + 'Dimes<br id="trailingDimeBr" />';
    }

    if(n == 0) {
        $('#trailingDimeBr').remove();
    } else if (n == 1) {
        out += '1 Nickel';
    }

    if (out == '') {
        out = 'No change'
    }
    $('#change').html(out);
    $('#money').text('$0.00');
    $('#messages').html('&emsp;');
    resetMessagesStatus()
}

function resetMessagesStatus() {
    $('#messages').removeClass('danger');
    $('#messages').removeClass('warning');
    $('#messages').removeClass('success');
}