var memberCache = [];
var valueCache = [];
var hideSelectBoxTask = null;  // 延迟隐藏选择框的任务
/*表格初始化*/
$(function(){
    $('#table').bootstrapTable({
        url: '/receipt',
        method: 'GET',
        pageNumber: 1,                  //初始化加载第一页，默认第一页
        uniqueId: "rowId",           // 表格唯一键
        pagination: true,               //是否显示分页（*）
        sidePagination: "server",       //分页方式：client客户端分页，server服务端分页（*）
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        contentType: 'application/json',
        toolbar: '#toolbar',
        showRefresh: true,   //是否显示刷新按钮
        showToggle: true,    //是否显示详细视图和列表视图的切换按钮
        showColumns: true,   //选择要显示的列
        striped: true,      //是否显示行间隔色
        toolbarAlign: 'left',//工具栏的位置
        clickToSelect: true,    //是否启用点击选中行
        cardView: false,     //是否显示详细视图
        detailView: false,   //是否显示父子表
        locale: 'zh-CN',    //配置中文汉化包
        maintainSelected: true, //翻页时保留已选中的行的状态
        queryParams: function (params) {    //传递参数（*）
            return {
                offset: params.offset,
                limit: params.limit,
                searchKey: $("#searchKey").val(),
                roomNo: $("#roomSearch").val(),
                voReceiptType: $("#voReceiptType").val(),
                voEffective: $("#effectiveSelect").val(),
            }
        },
        responseHandler: function (res) {
            return {total: res.data.total, rows: res.data.rows}
        },
        columns: [
        {
            checkbox: true,
            visible: true
        },
        {
            field: 'rowId',
            title: '收款单ID',
            visible: false,
        }, {
           field: 'receiptNumber',
           title: '收款单编号',
           align: "center",
       }, {
         field: 'receiptType',
         title: '款项类型',
         align: "center",
        }, {
        field: 'payType',
        title: '支付方式',
        align: "center",
       }, {
         field: 'amount',
         title: '收款金额',
         formatter: amountFormatter,
         align: "center",
     },{
          field: 'bizDate',
          title: '收款日期',
          formatter: dateFormatter,
          align: "center",
      },{
            field: 'leaseNumber',
            title: '租约编号',
            align: "center",
        }, {
            field: 'roomNo',
            title: '房间号',
            align: "center",
        }, {
            field: 'memberName',
            title: '租客',
            align: "center",
        }, {
            field: 'memberIdcard',
            title: '租客身份证号',
            align: "center",
            visible: false,
        },{
            field: 'memberTel',
            title: '租客手机号',
            align: "center",
            visible: false,
        }, {
            field: 'createTime',
            title: '创建时间',
            formatter: dateFormatter,
            align: "center",
        }, {
            field: 'lastUpdateTime',
            title: '最后修改时间',
            formatter: dateFormatter,
            align: "center",
        }],
        cellStyle: function (value, row, index) {
            return {css: {'background-color': '#F3F3F4'}};
        },
        onDblClickRow: function(row, $element) {
          // 点击行触发的操作
          openMadel(row, '查看租约');
          lockAllInfo();
          // 其他操作逻辑...
        },
        onLoadError: function (status, res) {
            var resJson = res.hasOwnProperty('responseJSON') ? res.responseJSON : null;
            var errMsg = resJson && resJson.msg ? resJson.msg : null;
            if (errMsg) swal("获取租约列表失败", errMsg, "error");
        }
    });
});
$('td[data-rowcolor]').attr("style", "background-color:yellow;");

<!--  租约信息表单校验规则  -->
function initValidate() {
    $(document).ready(function () {
        $('#addOrUpdateform').bootstrapValidator({
            <!--  excluded: [':disabled', ':hidden', ':not(:visible)', ':empty'],-->
            fields: {
                roomId: {
                    validators: {
                        notEmpty: {
                            message: '房间号不能为空'
                        }
                    }
                },
                amount: {
                    validators: {
                        notEmpty: {
                            message: '收款金额不能为空'
                        },
                        regexp: {
                            regexp: /^([1-9]\d{0,4}|\d{2,4})(\.\d{1,1})?$/,
                            message: '收款金额格式错误，请重新输入'
                        }
                    }
                },
                bizDate: {
                    validators: {
                        notEmpty: {
                            message: '开始日期不能为空'
                        }
                    }
                }
            }
        });
    });
}


$(document).ready(function () {
    // 在页面加载完成后执行的脚本
    initUsingRoom();
    initValidate();
});

/*初始化租用中房间下拉框*/
function initUsingRoom() {
    $.ajax("/room/roomList?"+"status=1", {
        type: 'get',
        dataType: "json",
        success: function (data) {
            roomList = data.data;
            var opts = "<option value=''>请选择房间</option>"; // 添加默认空选项
            for (var index = 0; index < roomList.length; index++) {
                var room = roomList[index];
                opts += "<option value='" + room.id + "'>" + room.number + '房' + "</option>";
            }
            $("#roomId").append(opts);
            // $("#roomId").selectpicker("refresh");
        }
    });
}

var memberSelect = document.getElementById("memberId");
memberSelect.addEventListener("change", function () {
    var memberSelectValue = memberSelect.value;
    console.log(memberSelectValue);
    // 获取当前选中的值
    $.ajax("/member/getById?" + "id=" + memberSelectValue, {
            type: 'get',
            dataType: "json",
            success: function (res) {
                member = res.data;
                $("#memberIdcard").val(member.idCard);
                $("#memberTel").val(member.tel);
            }
        });
});

var leaseSelect = document.getElementById("leaseId");
leaseSelect.addEventListener("change", function () {
    var leaseSelectValue = leaseSelect.value;
    console.log(leaseSelectValue);
    document.getElementById("memberId").innerHTML = "";
    // 获取当前选中的值
    $.ajax("/member/queryMemberByLeaseId?" + "id=" + leaseSelectValue, {
            type: 'get',
            dataType: "json",
            success: function (res) {
                memberList = res.data;
                var leaseOpts = ""; // 添加默认空选项
                var memberOpts = ""; // 添加默认空选项
                let uniqueLease = [];
                let firstLease = '';
                for (let index = 0; index < memberList.length; index++) {
                    let member = memberList[index];
                    memberOpts += "<option value='" + member.id + "'>" + member.name + "</option>";
                    if(index === 0){
                        $("#memberIdcard").val(member.idCard);
                        $("#memberTel").val(member.tel);
                    }
                }
                $("#memberId").append(memberOpts);
                // $("#roomId").selectpicker("refresh");
            }
        });
});

// 获取下拉框元素
var roomSelect = document.getElementById("roomId");
// 绑定 change 事件
roomSelect.addEventListener("change", function () {
// 清空下拉框内容
            document.getElementById("leaseId").innerHTML = "";
            document.getElementById("memberId").innerHTML = "";
    // 获取当前选中的值
    var roomSelectValue = roomSelect.value;
    console.log(roomSelectValue);
    $.ajax("/lease/getByRoomId?" + "roomId=" + roomSelectValue, {
            type: 'get',
            dataType: "json",
            success: function (res) {
                leaseList = res.data;
                var leaseOpts = ""; // 添加默认空选项
                var memberOpts = ""; // 添加默认空选项
                let uniqueLease = [];
                let firstLease = '';
                for (let index = 0; index < leaseList.length; index++) {
                    let lease = leaseList[index];
                    if(index === 0){
                        firstLease = lease.rowId;
                    }
                    if (!uniqueLease.includes(lease.rowId)) {
                        uniqueLease.push(lease.rowId);
                        leaseOpts += "<option value='" + lease.rowId + "'>" + lease.leaseNumber + "</option>";
                    }
                    if(firstLease === lease.rowId){
                        memberOpts += "<option value='" + lease.memberId + "'>" + lease.memberName + "</option>";
                        // 第一个租约的第一个租客的信息填充
                        if(index === 0){
                            $("#memberIdcard").val(lease.idCard);
                            $("#memberTel").val(lease.tel);
                        }
                    }
                }
                $("#leaseId").append(leaseOpts);
                $("#memberId").append(memberOpts);
                // $("#roomId").selectpicker("refresh");
            }
        });
});

// 获取下拉框元素
var effectiveSelect = document.getElementById("effectiveSelect");
// 绑定 change 事件
effectiveSelect.addEventListener("change", function () {
    console.log(effectiveSelect)
    // 获取当前选中的值
    var selectedValue = effectiveSelect.value;
    console.log(selectedValue);
    if (selectedValue === '0') {
//        $('#effective').show();
        $('#delete').show();
        $('#terminate').hide();
        $('#edit').hide();
        $('#new').hide();
        // 清除其余条件
        $('#voLeaseType').val('-1')
        $('#memberSearch').val('')
        $('#roomSearch').val('')
    } else if (selectedValue === '-1') {
//        $('#effective').show();
        $('#delete').show();
        $('#terminate').show();
        $('#edit').show();
        $('#new').show();
    } else {
//        $('#effective').hide();
        $('#delete').hide();
        $('#terminate').show();
        $('#edit').show();
        $('#new').show();
    }
    cleanSelectRows();
    $("#table").bootstrapTable('refresh');
});

//显示 - 隐藏下拉列表
$("#type_select_options").mouseleave( function(){
    $("#type_select_options").hide();
} );

function show_options()
{
    if( $("#type_select_options").is(":hidden") )
        $("#type_select_options").show();
    else
        $("#type_select_options").hide();
}

//复选框勾选，更新显示
function chk_changed(value) {
    var str_selected = "";
    var lstChk = document.getElementsByName("select_checkbox");
    var allCheckbox = document.getElementsByName("select_checkbox")[0];

    if (allCheckbox.checked) {
        // 如果全选时选取其它值，取消全选
        if (value !== '-1') {
            allCheckbox.checked = false;
            for( var n = 0; n < lstChk.length; n++ )
            {
                if( lstChk[n].checked )
                    str_selected = str_selected + "/" + lstChk[n].parentNode.textContent.trim();
            }
        } else {
            // 如果勾选了全部款项，则勾选所有复选框
            for (var n = 0; n < lstChk.length; n++) {
                lstChk[n].checked = true;
            }
            str_selected = '/全部款项';
        }
    } else {
        // 如果取消勾选全部款项，则取消勾选所有复选框
        if (value === '-1'){
            for (var n = 0; n < lstChk.length; n++) {
                lstChk[n].checked = false;
            }
            str_selected = '';
        }else {
            let allCheck = true;
            for( var n = 1; n < lstChk.length; n++ )
            {
                if( lstChk[n].checked ) {
                    str_selected = str_selected + "/" + lstChk[n].parentNode.textContent.trim();
                }else {
                    allCheck = false;
                }
            }
            if(allCheck){
                allCheckbox.checked = true;
                str_selected = '/全部款项';
            }
        }
    }
    $("#type_select").val(str_selected.substring(1, str_selected.length));

    var selectedValues = []
     if (allCheckbox.checked) {
        $("#voReceiptType").val('');
     }else{
        for (var i = 0; i < lstChk.length; i++) {
            if( lstChk[i].checked ){
                selectedValues.push(lstChk[i].value);
            }
        }
        var selectedString = selectedValues.join(",");
        $("#voReceiptType").val(selectedString);
     }
}

function addReceipt() {
    $('.modal-title').text("新增收款")
    $('#addOrUpdateModal').modal('show')
    $('#addOrUpdateform')[0].reset()  //重置表单

// 初始化时间
    var bizDate = new Date();
    var formattedStartDate = bizDate.toISOString().substring(0, 10);
    $("#bizDate").val(formattedStartDate);
}

$('#addOrUpdateModal').on('hidden.bs.modal', function () {
    document.getElementById("leaseId").innerHTML = "";
    document.getElementById("memberId").innerHTML = "";
});

function addOrUpdate() {
    let receiptId = $('#id').val();
    console.log("receiptId的值为：" + receiptId)
     var selectElement1 = document.getElementById("roomId");
      var roomNo = selectElement1.options[selectElement1.selectedIndex].innerHTML;
       var selectElement2 = document.getElementById("leaseId");
        var leaseNumber = selectElement2.options[selectElement2.selectedIndex].innerHTML;
        var selectElement3 = document.getElementById("memberId");
        var memberName = selectElement3.options[selectElement3.selectedIndex].innerHTML;
    var data = {
        rowId: receiptId,
        amount: $('#amount').val(),
        receiptType: $('#receiptType').val(),
        payType: $('#payType').val(),
        bizDate: $('#bizDate').val(),
        roomNo: roomNo,
        leaseNumber: leaseNumber,
        memberName: memberName,
        memberIdcard: $('#memberIdcard').val(),
        memberTel: $('#memberTel').val(),
    };
        // {# 如果不存在project_id就是新增 #}
        if (!receiptId) {
            $.ajax({
                type: "POST",
                url: "/receipt",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",  // 设置请求头部
                data: JSON.stringify(data),  // 设置请求体
                success: function (res) {
                    if (res.code == 200) {
                        swal("新 增", "收款记录已添加",
                            "success");
                    } else {
                        swal("添加失败", res.msg, "error")
                    }
                    // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                    $("#table").bootstrapTable('refresh');
                    $("#addOrUpdateform")[0].reset();
                    $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                    $('#addOrUpdateModal').modal('hide');
                    $("#table").bootstrapTable('refresh');
                },
                error: function (res) {
                    swal("添加失败", res.responseJSON.msg, "error")
                }
            })
        }
        // {# 如果project_id存在就是修改 #}
        else {
            var arr = [];
            arr.push(data);
            $.ajax({
                type: "PUT",
                url: "/receipt", // 待后端提供PUT修改接口
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(arr),  // 设置请求体
                success: function (res) {
                    console.log(res);
                    if (res.code == 200) {
                        swal("修 改", "收款信息已修改",
                            "success");
                        cleanSelectRows();
                    } else {
                        swal("修改失败", res.msg, "error")
                    }
                    // {#关闭模态框并清除框内数据，否则下次打开还是上次的数据#}
                    $("#table").bootstrapTable('refresh');
                    $("#addOrUpdateform")[0].reset();
                    $('#id').val('');   // 租客id作为隐藏字段无法通过reset()清除，需要单独处理
                    $('#addOrUpdateModal').modal('hide');
                    $("#table").bootstrapTable('refresh');
                },
                error: function (res) {
                    swal("修改失败", res.responseJSON.msg, "error")
                }
            })
        }
}