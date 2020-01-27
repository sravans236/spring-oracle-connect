<!DOCTYPE html>
<html lang="en">
<head>
    <title>Form to submit oracle queries</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <style>
       #tbl {
         font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
         border-collapse: collapse;
         width: 50%;
       }

       #tbl td, #tbl th {
         border: 1px solid #ddd;
         padding: 8px;
         text-align: center;
       }

       #tbl tr:nth-child(even){background-color: #f2f2f2;}

       #tbl tr:hover {background-color: #ddd;}

       #tbl th {
         padding-top: 12px;
         padding-bottom: 12px;
         text-align: center;
         background-color: #4CAF50;
         color: white;
       }
    </style>
</head>
<body>


<div class="container">
    <h2>Welcome to Oracle query editor</h2><br><br>


    <#if oracleData?? >
    <strong> Search successful </strong><br><br>
    <!-- JSON Result set: ${oracleData.output} <br> -->
    <input type="button" onclick='CreateTableFromJSON(${oracleData.output})' value="Display Table"/><br><br>

    <table id="tbl"></table>
    <div id="showData"></showData>

    <#else>

    <form action="/search" method="post">
        <div class="form-group">
            <label>Query:</label>
            <input type="text" class="form-control" name="input" placeholder="Enter sql">
        </div>

        <button type="submit" class="btn btn-default">Submit</button>

    </form>
    </#if>
</div>

<script>
    function CreateTableFromJSON(input) {
    console.log(input)

        var myBooks = input
        console.log(myBooks)
        // EXTRACT VALUE FOR HTML HEADER.
        // ('Book ID', 'Book Name', 'Category' and 'Price')
        var col = [];
        for (var i = 0; i < myBooks.length; i++) {
            for (var key in myBooks[i]) {
                if (col.indexOf(key) === -1) {
                    col.push(key);
                }
            }
        }

        // CREATE DYNAMIC TABLE.
        // var table = document.createElement("table");
        var table = document.getElementById("tbl");

        // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

        var tr = table.insertRow(-1);                   // TABLE ROW.

        for (var i = 0; i < col.length; i++) {
            var th = document.createElement("th");      // TABLE HEADER.
            th.innerHTML = col[i];
            tr.appendChild(th);
        }

        // ADD JSON DATA TO THE TABLE AS ROWS.
        for (var i = 0; i < myBooks.length; i++) {

            tr = table.insertRow(-1);

            for (var j = 0; j < col.length; j++) {
                var tabCell = tr.insertCell(-1);
                tabCell.innerHTML = myBooks[i][col[j]];
            }
        }

        // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.
        var divContainer = document.getElementById("showData");
        divContainer.innerHTML = "";
        divContainer.appendChild(table);
    }
</script>

</body>
</html>