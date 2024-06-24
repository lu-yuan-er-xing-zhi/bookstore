<template>
  <div class="app-container">
    <!-- 顶部功能 -->
    <div class="filter-container" style="margin-bottom: 15px">

      <!-- 图书名输入 -->
      <el-input
        v-model="queryParam.bookname"
        placeholder="图书名"
        style="width: 200px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <br /><br />
      <!-- 一些按钮 -->
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        style="font-size: 18px"
        @click="handleFilter"
      >

        搜索
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        style="font-size: 18px"
        @click="handleShowAll"
      >

        显示全部
      </el-button>

    </div>

    <!--数据表格-->
    <el-table
      ref="multipleTable"
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column fixed type="selection" width="55">
      </el-table-column>

      <el-table-column fixed prop="buyId" label="购买单号" width="100">
      </el-table-column>
      <el-table-column
        prop="bookName"
        label="图书名"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column prop="buyNum" label="购买数量">
      </el-table-column>
      <el-table-column prop="bookPrice" label="购买价格">
      </el-table-column>

      <el-table-column prop="buyTime" label="购买时间" sortable>
      </el-table-column>


      <el-table-column
        v-if="!roleIsAdmin"
        fixed="right"
        label="操作"
        :width=" '180px'"
      >
        <template slot-scope="scope">

          <el-button
            v-if="!roleIsAdmin"
            @click="gotoPayInfo"
            type="success"
            style="font-size: 22px"
          >再次购买</el-button>
        </template>
      </el-table-column>




    </el-table>

    <!--分页条-->
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="queryParam.page"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="queryParam.limit"
      layout="total, sizes, prev, pager, next, jumper"
      :total="recordTotal"
      style="margin-top: 15px"
    >
    </el-pagination>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import permission from "@/directive/permission/index.js"; // 权限判断指令
import waves from "@/directive/waves"; // waves directive
import {
  getCount,
  queryBorrows,
  queryBorrowsByPage,
  addBorrow,
  deleteBorrow,
  deleteBorrows,
  updateBorrow,
  returnBook,
} from "@/api/borrow";
import {
  // getCount,
  queryBookInfosByPage,
  addBookInfo,
  deleteBookInfo,
  deleteBookInfos,
  updateBookInfo,
} from "@/api/bookinfo";

export default {
  name: "Bookinfo",
  directives: { waves, permission },
  // 创建后
  created() {
    // 从服务器获取数据表格第一页的信息
    queryBorrowsByPage(this.queryParam).then((res) => {
      console.log("首页数据获取成功", res);
      this.tableData = res.data;
      this.recordTotal = res.count;
      ;
    });
  },
  methods: {
    // 分页大小改变监听
    handleSizeChange(curSize) {
      const params = this.queryParam;
      params.limit = curSize;
      queryBorrowsByPage(params).then((res) => {
        console.log("分页数据获取成功", res);
        this.tableData = res.data;
        this.recordTotal = res.count;
        ;
      });
    },

    // 点击分页监听方法
    handleCurrentChange(curPage) {
      const params = this.queryParam;
      params.page = curPage;
      queryBorrowsByPage(params).then((res) => {
        console.log("分页数据获取成功", res);
        this.tableData = res.data;
        this.recordTotal = res.count;
        ;
      });
    },

    // 搜索
    handleFilter() {
      this.queryParam.page = 1;
      queryBorrowsByPage(this.queryParam).then((res) => {
        if (res.code === 0) {
          this.tableData = res.data;
          this.recordTotal = res.count;
        }
        ;
      });
    },

    // 显示全部
    handleShowAll() {
      this.queryParam.page = 1;
      this.queryParam.username = null;
      this.queryParam.bookname = null;
      queryBorrowsByPage(this.queryParam).then((res) => {
        if (res.code === 0) {
          this.tableData = res.data;
          this.recordTotal = res.count;
        }
        ;
      });
    },


    gotoPayInfo(){
      this.$router.push('/payinfo');

    },
    // 还书
    handleReturn(row, index) {
      this.$confirm("确定要购买吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        returnBook(row.borrowid, row.bookid).then((res) => {
          if (res === 1) {
            this.$message.success("购买成功");
            window.location.href ="this.$router.push('/pay')"
            this.handleCurrentChange(this.queryParam.page);
          } else if (res === 0) {
            this.$message.error("该图书已经是归还的状态");
          } else {
            this.$message.error("还书失败");
          }
        });
      });
    },


  },
  mounted() {

  },
  data() {
    return {
      // 表格数据
      tableData: [],
      // 记录总数
      recordTotal: 0,
      // 查询参数
      queryParam: {
        page: 1,
        limit: 10,
        userid: null,
        username: null,
        bookname: null,
        // capacity: null,
      },
    };
  },
  computed: {
    // 获得user信息
    ...mapGetters(["id", "name", "roles"]),
    roleIsAdmin() {
      if (this.roles[0] === "admin") return true;
      else return false;
    },
  },
  watch: {
    "queryParam.userid": {
      immediate: true,
      handler() {
        if (this.roleIsAdmin) {
          this.queryParam.userid = null;
        } else {
          this.queryParam.userid = this.id;
        }
      },
    },
  },
};
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 150px;
  height: 200px;
  display: block;
}
</style>
