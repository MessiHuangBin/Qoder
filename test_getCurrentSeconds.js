// 自动化测试 getCurrentSeconds 方法
const fetch = require('node-fetch');

async function testGetCurrentSeconds() {
    const baseUrl = 'http://localhost:8080';
    const endpoint = '/time/current-seconds';
    const fullUrl = baseUrl + endpoint;
    
    console.log('🚀 开始测试 getCurrentSeconds 方法');
    console.log('📍测试URL:', fullUrl);
    console.log('⏰测试时间:', new Date().toLocaleString('zh-CN'));
    console.log('----------------------------------------');
    
    try {
        // 发送GET请求
        const response = await fetch(fullUrl);
        const statusCode = response.status;
        const statusText = response.statusText;
        
        console.log('📡 HTTP响应状态:', statusCode, statusText);
        
        if (!response.ok) {
            console.error('❌ HTTP请求失败:', statusCode, statusText);
            return;
        }
        
        // 解析JSON响应
        const data = await response.json();
        console.log('✅响应数据:', JSON.stringify(data, null, 2));
        
        //验证响应结构
        console.log('\n📋验证响应结构:');
        const requiredFields = ['currentSeconds', 'currentTime', 'timestamp', 'message', 'status'];
        let allFieldsPresent = true;
        
        for (const field of requiredFields) {
            if (data.hasOwnProperty(field)) {
                console.log(`✅ 字段 "${field}"存:`, data[field]);
            } else {
                console.log(`❌ 字段 "${field}"缺失`);
                allFieldsPresent = false;
            }
        }
        
        //验证数据逻辑
        console.log('\n🔍验证数据逻辑:');
        if (typeof data.currentSeconds === 'number') {
            if (data.currentSeconds >= 0 && data.currentSeconds <= 59) {
                console.log(`✅ currentSeconds值 (${data.currentSeconds}) - 在 0-59内`);
            } else {
                console.log(`❌ currentSeconds值 (${data.currentSeconds}) - 不在 0-59 范围内`);
            }
        } else {
            console.log(`❌ currentSeconds 类型错误 (${typeof data.currentSeconds})`);
        }
        
        if (data.status === 'success') {
            console.log('✅状态正确:', data.status);
        } else {
            console.log('❌状态错误:', data.status);
        }
        
        if (data.message && data.message.includes('秒数')) {
            console.log('✅消息内容正确:', data.message);
        } else {
            console.log('❌消息内容可能不正确:', data.message);
        }
        
        // 时间一致性检查
        console.log('\n⏱️  时间一致性检查:');
        const currentTimeFromResponse = new Date(data.timestamp);
        const currentTimeFromSystem = new Date();
        const timeDifference = Math.abs(currentTimeFromResponse.getTime() - currentTimeFromSystem.getTime());
        
        console.log('系统当前时间:', currentTimeFromSystem.toLocaleString('zh-CN'));
        console.log('响应时间戳:', currentTimeFromResponse.toLocaleString('zh-CN'));
        console.log('时间差值:', timeDifference, '毫秒');
        
        if (timeDifference < 5000) { // 5秒内认为是一致的
            console.log('✅ 时间基本一致');
        } else {
            console.log('⚠️  时间差异较大，可能存在问题');
        }
        
        //格验证
        console.log('\n📄格式验证:');
        const timeRegex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;
        if (timeRegex.test(data.currentTime)) {
            console.log('✅ 时间格式正确:', data.currentTime);
        } else {
            console.log('❌ 时间格式错误:', data.currentTime);
        }
        
        console.log('\n----------------------------------------');
        if (allFieldsPresent) {
            console.log('🎉 测试通过！getCurrentSeconds 方法工作正常');
        } else {
            console.log('⚠️ 测试部分通过，存在字段缺失问题');
        }
        
    } catch (error) {
        console.error('💥测试执行失败:', error.message);
        console.error('详细错误:', error);
    }
}

//执行测试
testGetCurrentSeconds();