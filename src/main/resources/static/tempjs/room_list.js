/*表格初始化*/
$('#table').bootstrapTable({
    url: '/room',
    method: 'GET',
    pageNumber: 1,                  //初始化加载第一页，默认第一页
    uniqueId: "roomId",           // 表格唯一键
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
    detailView: true,   //是否显示父子表
    locale: 'zh-CN',    //配置中文汉化包
    maintainSelected: true, //翻页时保留已选中的行的状态
    queryParams: function (params) {    //传递参数（*）
        return {
            offset: params.offset,
            limit: params.limit,
//            roomStatus: document.getElementById("roomStatus").value,
//            roomNo: $("#roomSearch").val(),
//            memberName: $("#memberSearch").val()
        }
    },
    responseHandler: function (res) {
        return {total: res.data.total, rows: res.data.rows}
    },
    columns: [{
        checkbox: true,
        visible: true
    }, {
        field: 'roomId',
        title: '房间ID',
        visible: false
    }, {

        field: 'roomNo',
        title: '房间号',
        align: "center", //表内容居中
    }, {
        field: 'memberName',
        title: '租客姓名',
        align: "center", //表内容居中
        // visible: false
    }, {
        field: 'roomStatus',
        title: '房间状态',
        align: "center", //表内容居中
    }, {
        field: 'roomDesc',
        title: '房间描述',
        align: "center", //表内容居中
    }],
    cellStyle: function (value, row, index) {
        return {css: {'background-color': '#F3F3F4'}};
    }
});