$(document).ready(function () {
                $.getJSON("file.json", function (data) {

                    var arrItems = [];
                    $.each(data, function (index, value) {
                        arrItems.push(value);
                    });


                    var col = [];
                    for (var i = 0; i < arrItems.length; i++) {
                        for (var key in arrItems[i]) {
                            if (col.indexOf(key) === -1) {
                                col.push(key);
                            }
                        }
                    }

                    var table = document.createElement("table");
                    var tr = table.insertRow(-1);

                    for (var i = 0; i < col.length; i++) {
                        var th = document.createElement("th");
                        th.innerHTML = col[i];
                        tr.appendChild(th);
                    }


                    for (var i = 0; i < arrItems.length; i++) {

                        tr = table.insertRow(-1);

                        for (var j = 0; j < col.length; j++) {
                            var tabCell = tr.insertCell(-1);
                            tabCell.innerHTML = arrItems[i][col[j]];
                        }
                    }

                    var divContainer = document.getElementById("welcome");
                    divContainer.innerHTML = "";
                    divContainer.appendChild(table);
                });
            });