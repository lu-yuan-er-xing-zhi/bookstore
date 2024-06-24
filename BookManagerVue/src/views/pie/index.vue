<template>
  <div id="pie-chart" style="width: 600px;height:400px;"></div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios'; // 假设使用 Axios 进行HTTP请求
import {
  // ... (其他导入保持不变)
  queryBookInfos
} from "@/api/bookinfo";

// 注意：这里只保留一个异步版本的 getPieChartData 函数
async function getPieChartData() {
  try {
    const response = await axios.get('http://localhost:9111/BookManager/bookInfo/queryBookInfos');
    console.log(response)
    if (response &&  response.data) {
      const formattedData = response.data.map(item => ({
        name: item.bookname, // 假设后端的标题字段为title
        value: item.book_num // 假设后端的数量字段为count
      }));
      return formattedData;
      
    } else {
      console.error('获取数据失败');
      return [];
    }
  } catch (error) {
    console.error('请求数据时发生错误', error);
    return [];
  }
}

export default {
  data() {
    return {
      chartInstance: null,
      pieData: [
        
      ]
    };
  },

  methods: {
    async initChart() {
      const dataFromBackend = await getPieChartData();
      console.log(dataFromBackend);
      if (dataFromBackend && dataFromBackend.length > 0) {
        this.pieData = dataFromBackend.map(item => ({ value: item.value, name: item.name }));
      }
      this.chartInstance = echarts.init(document.getElementById('pie-chart'));

      const option = {
        tooltip: {
          trigger: 'item'
        },
        // legend: {
        //   orient: 'vertical',
        //   left: 'left'
        // },
        series: [{
          name: '访问来源',
          type: 'pie',
          radius: '50%',
          data: this.pieData,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      };
      this.chartInstance.setOption(option);
    }
  },

  mounted() {
    this.initChart();
  },

  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  }
};
</script>