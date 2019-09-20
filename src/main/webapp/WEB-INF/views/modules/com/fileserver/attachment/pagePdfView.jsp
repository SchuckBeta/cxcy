<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${fileName}</title>
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <style type="text/css">
        html,body{
            width: 100%;
            height: 100%;
            overflow: hidden;
        }
        body{
            margin: 0;
            padding: 0;
        }
    </style>
</head>

<body>
<embed style="width: 100%;height: 100%;" type="application/pdf" name="plugin" src="${url}"></embed>
<script>
    $(function () {
        $(document).on('keydown', function (e) {
            e.preventDefault();
            if(e.ctrlKey && e.keyCode == 83 ){
                location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=${ftpUrl}&fileName=${fileName}";
            }
        })
    })

</script>
</body>
</html>