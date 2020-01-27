function toTable(json, colKeyClassMap, rowKeyClassMap){
    let tab;
    if(typeof colKeyClassMap === 'undefined'){
        colKeyClassMap = {};
    }
    if(typeof rowKeyClassMap === 'undefined'){
        rowKeyClassMap = {};
    }

    const newTable = '<table class="table table-bordered table-condensed table-striped" />';
    if($.isArray(json)){
        if(json.length === 0){
            return '[]'
        } else {
            const first = json[0];
            if($.isPlainObject(first)){
                tab = $(newTable);
                const row = $('<tr />');
                tab.append(row);
                $.each( first, function( key, value ) {
                    row.append($('<th />').addClass(colKeyClassMap[key]).text(key))
                });

                $.each( json, function( key, value ) {
                    const row = $('<tr />');
                    $.each( value, function( key, value ) {
                        row.append($('<td />').addClass(colKeyClassMap[key]).html(toTable(value, colKeyClassMap, rowKeyClassMap)))
                    });
                    tab.append(row);
                });

                return tab;
            } else if ($.isArray(first)) {
                tab = $(newTable);

                $.each( json, function( key, value ) {
                    const tr = $('<tr />');
                    const td = $('<td />');
                    tr.append(td);
                    $.each( value, function( key, value ) {
                        td.append(toTable(value, colKeyClassMap, rowKeyClassMap));
                    });
                    tab.append(tr);
                });

                return tab;
            } else {
                return json.join(", ");
            }
        }

    } else if($.isPlainObject(json)){
        tab = $(newTable);
        $.each( json, function( key, value ) {
            tab.append(
                $('<tr />')
                    .append($('<th style="width: 20%;"/>').addClass(rowKeyClassMap[key]).text(key))
                    .append($('<td />').addClass(rowKeyClassMap[key]).html(toTable(value, colKeyClassMap, rowKeyClassMap))));
        });
        return tab;
    } else if (typeof json === 'string') {
        if(json.slice(0, 4) === 'http'){
            return '<a target="_blank" href="'+json+'">'+json+'</a>';
        }
        return json;
    } else {
        return ''+json;
    }
    var divContainer = document.getElementById("showData");
                divContainer.innerHTML = "";
                divContainer.appendChild(tab);
};