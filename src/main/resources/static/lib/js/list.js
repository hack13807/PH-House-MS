function searchMember(memberName,roomNo,pn){
        var memberName = document.getElementById("memberSearch").value;
        var roomNo = document.getElementById("roomSearch").value;
        var pn = document.getElementById("pageNum").value;
        var condition = {
            'pn': pn,
            'memberName': memberName,
            'roomNo': roomNo
        };
        var JSONdata = JSON.stringify(condition);
        $.ajax({
            type: "post",
            url: "/member/search",
            data: JSONdata,
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            success: function (result) {
                if (result['success']) {
                    alert("添加成功");
                } else {
                    alert("添加失败");
                }
            }
        });
}