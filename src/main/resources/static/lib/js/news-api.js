/**
 * 新闻API和UI处理模块
 */
(function() {
    'use strict';
    
    // API配置
    const API_CONFIG = {
        workflow_id: '7474599559693778982',
        app_id: '7474543679249334309',
        proxy_url: '/api/proxy/coze',  // 替换为后端代理URL
        news_api_url: '/api/proxy/news' // 新增后端新闻API专用URL
    };

    // 页面状态管理
    const PAGE_STATE = {
        allNewsData: [] // 存储所有新闻数据
    };

    // 获取页面元素
    let newsContent = null;
    
    // 等待页面完全加载后再执行
    window.onload = function() {
        // 获取新闻内容元素
        newsContent = document.getElementById('newsContent');
        if (newsContent) {
            // 获取新闻数据
            fetchNews();
        } else {
            console.error('未找到newsContent元素');
        }
    };

    // 格式化日期时间
    function formatDateTime(dateStr) {
        if (!dateStr) return '未知时间';

        let date;
        try {
            date = new Date(dateStr);
            if (isNaN(date.getTime())) {
                console.warn('无效的日期字符串:', dateStr);
                return '未知时间';
            }
        } catch (e) {
            console.error('日期转换错误:', e);
            return '未知时间';
        }

        // 格式化为 yyyy-MM-dd HH:mm
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }

    // 创建新闻元素 - 基础版本
    function createNewsElement(news) {
        if (!news || !news.title) {
            const emptyEl = document.createElement('div');
            emptyEl.className = 'no-data';
            emptyEl.textContent = '暂无更新';
            return emptyEl;
        }

        const newsItem = document.createElement('div');
        newsItem.className = 'news-item';

        const titleEl = document.createElement('div');
        titleEl.className = 'news-item-title';
        titleEl.textContent = fixChineseText(news.title); // 修复中文文本

        const timeEl = document.createElement('div');
        timeEl.className = 'news-item-time';
        timeEl.textContent = formatDateTime(news.time);

        newsItem.appendChild(titleEl);
        newsItem.appendChild(timeEl);

        if (news.link) {
            titleEl.style.cursor = 'pointer';
            titleEl.addEventListener('click', () => {
                window.open(news.link, '_blank');
            });
        }

        return newsItem;
    }
    
    // 创建新闻卡片元素 - 带图片的卡片式列表
    function createNewsCard(news) {
        if (!news || !news.title) {
            const emptyEl = document.createElement('div');
            emptyEl.className = 'no-data';
            emptyEl.textContent = '暂无更新';
            return emptyEl;
        }

        // 修复新闻数据中可能的编码问题
        const fixedNews = {
            title: fixChineseText(news.title),
            time: news.time || new Date().toISOString(), // 确保有时间，没有则使用当前时间
            link: news.link,
            picture: news.picture
        };

        const newsCard = document.createElement('div');
        newsCard.className = 'news-card';
        
        // 如果有图片，则添加图片元素
        if (fixedNews.picture) {
            const imgContainer = document.createElement('div');
            imgContainer.className = 'news-img-container';
            
            const imgEl = document.createElement('img');
            imgEl.className = 'news-img';
            imgEl.src = fixedNews.picture;
            imgEl.alt = fixedNews.title;
            imgEl.loading = 'lazy'; // 延迟加载图片以提高性能
            imgEl.onerror = function() {
                // 图片加载失败时，隐藏图片容器
                imgContainer.style.display = 'none';
            };
            
            imgContainer.appendChild(imgEl);
            newsCard.appendChild(imgContainer);
        }
        
        const contentContainer = document.createElement('div');
        contentContainer.className = 'news-content';
        
        const titleEl = document.createElement('div');
        titleEl.className = 'news-item-title';
        titleEl.textContent = fixedNews.title;
        
        // 创建简单的时间元素
        const timeEl = document.createElement('div');
        timeEl.className = 'news-item-time';
        
        // 获取格式化的时间并确保显示
        let formattedTime = formatDateTime(fixedNews.time);
        // 确保始终有合理的时间显示
        if (!formattedTime || formattedTime.length < 2) {
            formattedTime = '未知时间';
        }
        
        // 使用更简洁的时钟图标和格式化时间
        timeEl.innerHTML = `<span>⏱ ${formattedTime}</span>`;
        
        // 添加所有元素到容器
        contentContainer.appendChild(titleEl);
        contentContainer.appendChild(timeEl);
        newsCard.appendChild(contentContainer);
        
        // 添加点击事件，点击整个卡片都会打开链接
        if (fixedNews.link) {
            newsCard.style.cursor = 'pointer';
            newsCard.addEventListener('click', () => {
                window.open(fixedNews.link, '_blank');
            });
        }
        
        return newsCard;
    }
    
    // 解码HTML实体，处理可能的中文乱码问题
    function decodeHtmlEntities(text) {
        if (!text) return '';
        
        // 创建一个临时元素来解码HTML实体
        const textArea = document.createElement('textarea');
        textArea.innerHTML = text;
        return textArea.value;
    }
    
    // 修复中文文本编码问题
    function fixChineseText(text) {
        if (!text) return '';
        
        // 首先解码HTML实体
        let result = decodeHtmlEntities(text);
        
        // 检查是否包含乱码特征（问号替换）
        if (result.includes('?') && /\?\?+/.test(result)) {
            try {
                // 尝试使用UTF-8重新解码
                const decoded = decodeURIComponent(escape(result));
                if (decoded && decoded !== result && !decoded.includes('??')) {
                    console.log('UTF-8解码成功:', decoded);
                    return decoded;
                }
            } catch (e) {
                console.error('重新解码失败:', e);
            }
            
            // 替换连续多个问号为单个问号
            result = result.replace(/\?{2,}/g, '?');
            
            // 移除特殊乱码字符
            result = result.replace(/[\uFFFD\uD800-\uDBFF]/g, '');
        }
        
        return result;
    }

    // 渲染新闻列表 - 基础版本
    function renderNews(newsData) {
        if (!newsContent) {
            console.error('找不到新闻容器元素');
            return;
        }
        
        newsContent.innerHTML = '';
        newsContent.className = 'news-list';

        if (!newsData || !Array.isArray(newsData) || newsData.length === 0) {
            newsContent.appendChild(createNewsElement(null));
            return;
        }

        // 克隆数组以避免修改原始数据
        const sortedNews = [...newsData];
        
        // 按时间倒序排序
        sortedNews.sort((a, b) => {
            if (!a.time) return 1;
            if (!b.time) return -1;
            
            const dateA = new Date(a.time);
            const dateB = new Date(b.time);
            
            if (isNaN(dateA.getTime()) && isNaN(dateB.getTime())) {
                return 0;
            } else if (isNaN(dateA.getTime())) {
                return 1;
            } else if (isNaN(dateB.getTime())) {
                return -1;
            }
            
            return dateB.getTime() - dateA.getTime();
        });

        // 只显示前5条
        const newsList = sortedNews.slice(0, 5);
        newsList.forEach(news => {
            newsContent.appendChild(createNewsElement(news));
        });
    }
    
    // 渲染新闻卡片列表 - 卡片式布局
    function renderNewsCards(newsData) {
        if (!newsContent) {
            console.error('找不到新闻容器元素');
            return;
        }
        
        newsContent.innerHTML = '';
        newsContent.className = 'news-card-container';

        if (!newsData || !Array.isArray(newsData) || newsData.length === 0) {
            newsContent.appendChild(createNewsCard(null));
            return;
        }

        // 打印原始数据，用于调试
        console.log('原始新闻数据:', JSON.stringify(newsData));
        
        // 预处理数据，修复可能的中文编码问题
        const processedNewsData = newsData.map(news => {
            if (news && typeof news === 'object') {
                return {
                    ...news,
                    title: news.title ? fixChineseText(news.title) : '无标题'
                };
            }
            return news;
        });
        
        // 按时间倒序排序（最新的在前）
        processedNewsData.sort((a, b) => {
            if (!a.time) return 1;  // 没有时间的排后面
            if (!b.time) return -1; // 没有时间的排后面
            
            // 尝试将时间转换为Date对象
            const dateA = new Date(a.time);
            const dateB = new Date(b.time);
            
            // 检查是否为有效日期
            if (isNaN(dateA.getTime()) && isNaN(dateB.getTime())) {
                return 0; // 两个都是无效日期，保持原顺序
            } else if (isNaN(dateA.getTime())) {
                return 1; // a是无效日期，排后面
            } else if (isNaN(dateB.getTime())) {
                return -1; // b是无效日期，排后面
            }
            
            // 正常比较，倒序排列
            return dateB.getTime() - dateA.getTime();
        });
        
        console.log('处理后的数据共', processedNewsData.length, '条');
        console.log('处理后的第一条数据:', processedNewsData[0]);
        
        // 更新页面状态
        PAGE_STATE.allNewsData = processedNewsData;
        
        // 直接显示所有新闻数据，不需要分页
        processedNewsData.forEach(news => {
            newsContent.appendChild(createNewsCard(news));
        });
        
        // 添加数据来源标记
        const isMockData = processedNewsData[0] && processedNewsData[0].title && processedNewsData[0].title.includes('模拟');
        if (isMockData) {
            const mockHint = document.createElement('div');
            mockHint.className = 'text-center text-muted';
            mockHint.style.padding = '10px';
            mockHint.style.marginTop = '15px';
            mockHint.textContent = '(使用模拟数据)';
            newsContent.appendChild(mockHint);
        }
    }

    // 获取新闻数据
    async function fetchNews() {
        try {
            if (!newsContent) {
                console.error('找不到新闻容器元素');
                return;
            }
            
            // 使用更明确的HTML结构，确保加载提示正确显示
            newsContent.innerHTML = `
                <div class="loading">
                    <div class="loading-spinner"></div>
                    <div class="loading-text">正在整合全网最新的AI大模型资讯，请稍候...</div>
                </div>
            `;

            console.log('开始获取新闻数据...');

            try {
                await fetchFromProxyApi();
            } catch (proxyError) {
                console.error('代理API请求失败:', proxyError);
                renderNewsCards(createMockNewsData('所有API调用方式均失败，显示模拟新闻'));
            }

        } catch (error) {
            console.error('获取新闻数据失败:', error);
            showError(error.message || '未知错误');
        }
    }
    
    // 从代理API获取数据
    async function fetchFromProxyApi() {
        console.log('从代理API获取数据...');
        
        // 设置超时控制
        const abortController = new AbortController();
        const timeoutId = setTimeout(() => {
            abortController.abort();
            console.error('代理API请求超时');
            throw new Error('请求超时，请检查网络连接');
        }, 15000);
        
        try {
            // 调用代理API
            const response = await fetch(API_CONFIG.proxy_url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=UTF-8',
                    'Accept': 'application/json;charset=UTF-8'
                },
                body: JSON.stringify({
                    workflow_id: API_CONFIG.workflow_id,
                    app_id: API_CONFIG.app_id,
                    parameters: {}
                }),
                signal: abortController.signal
            });
            
            clearTimeout(timeoutId);
            
            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`代理API错误 (${response.status}): ${errorText}`);
            }
            
            const data = await response.json();
            console.log('代理API响应:', data);
            
            // 处理响应数据
            handleApiResponse(data);
        } catch (error) {
            clearTimeout(timeoutId);
            console.error('代理API调用失败:', error);
            throw error;
        }
    }

    // 显示错误信息
    function showError(message, replace = true) {
        if (!newsContent) return;
        
        if (replace) {
            newsContent.innerHTML = '';
        }
        
        const errorContainer = document.createElement('div');
        errorContainer.className = 'error-container';
        
        const errorIcon = document.createElement('div');
        errorIcon.innerHTML = '&#9888;'; // 警告符号
        errorIcon.style.fontSize = '28px';
        errorIcon.style.color = '#ff4d4f';
        errorIcon.style.marginBottom = '10px';
        
        const errorMsg = document.createElement('div');
        errorMsg.className = 'error-message';
        errorMsg.textContent = `获取新闻失败: ${message}`;
        
        const errorDetails = document.createElement('div');
        errorDetails.className = 'error-details';
        errorDetails.textContent = '请检查网络连接或稍后再试';
        
        const retryBtn = document.createElement('button');
        retryBtn.textContent = '重试';
        retryBtn.className = 'btn btn-sm btn-danger';
        retryBtn.style.marginRight = '10px';
        retryBtn.onclick = function() {
            fetchNews();
        };
        
        const useBackupBtn = document.createElement('button');
        useBackupBtn.textContent = '使用备用数据';
        useBackupBtn.className = 'btn btn-sm btn-default';
        useBackupBtn.onclick = function() {
            renderNewsCards(createMockNewsData('使用备用数据'));
        };
        
        errorContainer.appendChild(errorIcon);
        errorContainer.appendChild(errorMsg);
        errorContainer.appendChild(errorDetails);
        errorContainer.appendChild(retryBtn);
        errorContainer.appendChild(useBackupBtn);
        newsContent.appendChild(errorContainer);
    }
    
    // 创建模拟新闻数据（当API调用失败时使用）
    function createMockNewsData(message) {
        const newsList = [];
        
        // 中文模拟数据
        const titles = [
            message, 
            '人工智能技术最新突破，自然语言处理能力再上新台阶',
            '科学家发现新型可再生能源技术，有望解决全球能源危机',
            '远程办公新趋势：企业数字化转型加速推进',
            '量子计算研究取得重大进展，打破传统算力限制',
            '全球气候变化应对策略：各国共同努力减少碳排放',
            '区块链技术在供应链管理中的创新应用',
            '虚拟现实技术进入医疗领域，手术培训效果显著提升'
        ];
        
        // 创建更多模拟数据，用于测试滚动功能
        const extendedTitles = [];
        for (let i = 0; i < 5; i++) {
            titles.forEach((title, index) => {
                if (index > 0) { // 跳过第一条消息标题
                    extendedTitles.push(`${title} - 系列${i+1}`);
                }
            });
        }
        
        // 合并标题
        const allTitles = [titles[0], ...extendedTitles];
        
        // 获取当前时间
        const now = new Date();
        
        // 创建新闻数据，使每条数据的时间依次往前推
        for (let i = 0; i < allTitles.length; i++) {
            // 计算模拟时间：当前时间往前推i小时
            const mockTime = new Date(now.getTime() - i * 3600000);
            
            newsList.push({
                title: allTitles[i],
                time: mockTime.toISOString(),
                link: 'https://www.coze.cn',
                picture: `https://picsum.photos/600/400?random=${i}` // 更大尺寸的图片
            });
        }
        
        // 按时间倒序排序
        newsList.sort((a, b) => {
            const dateA = new Date(a.time);
            const dateB = new Date(b.time);
            return dateB.getTime() - dateA.getTime();
        });
        
        return newsList;
    }

    // 从API响应中提取新闻数据
    function extractNewsFromResponse(data) {
        console.log('尝试从响应中提取新闻数据:', data);
        
        // 处理标准格式响应
        if (data && data.code === 0 && data.data) {
            try {
                let parsedData;
                
                // 如果data字段是字符串，尝试解析
                if (typeof data.data === 'string') {
                    try {
                        parsedData = JSON.parse(data.data);
                        console.log('成功解析data字符串为JSON:', parsedData);
                    } catch (e) {
                        console.error('解析data字段失败:', e);
                        // 尝试手动清理JSON字符串中的乱码或特殊字符
                        const cleanedData = data.data
                            .replace(/[\uFFFD\uD800-\uDBFF]/g, '') // 移除无效的Unicode字符
                            .replace(/\\+"/g, '\\"'); // 修复可能的转义问题
                            
                        try {
                            parsedData = JSON.parse(cleanedData);
                            console.log('通过清理后成功解析data字符串:', parsedData);
                        } catch (e2) {
                            console.error('清理后仍然无法解析:', e2);
                            return null;
                        }
                    }
                } else {
                    parsedData = data.data;
                }
                
                // 尝试查找news_list字段
                if (parsedData.news_list && Array.isArray(parsedData.news_list)) {
                    console.log('找到news_list字段, 包含', parsedData.news_list.length, '条新闻');
                    
                    // 添加标题长度检查，过滤掉没有标题的条目
                    const validNews = parsedData.news_list.filter(item => 
                        item && item.title && item.title.trim().length > 0
                    ).map(item => {
                        // 确保每条新闻都有时间字段
                        if (!item.time) {
                            console.log('新闻条目没有时间字段，添加当前时间:', item.title);
                            item.time = new Date().toISOString();
                        }
                        return item;
                    });
                    
                    console.log('提取的有效新闻数据:', validNews);
                    
                    if (validNews.length === 0) {
                        console.warn('过滤后没有有效的新闻条目');
                        return null;
                    }
                    
                    return validNews;
                }
            } catch (e) {
                console.error('处理标准格式响应失败:', e);
            }
        }
        
        // 其他可能的数据格式
        if (data && Array.isArray(data)) {
            // 如果直接是数组格式
            return ensureTimeField(data);
        } else if (data && data.news && Array.isArray(data.news)) {
            // 如果在news字段中
            return ensureTimeField(data.news);
        } else if (data && data.data && data.data.news && Array.isArray(data.data.news)) {
            // 嵌套在data.data.news中
            return ensureTimeField(data.data.news);
        } else if (typeof data === 'string') {
            // 尝试解析JSON字符串
            try {
                const parsedData = JSON.parse(data);
                return extractNewsFromResponse(parsedData);
            } catch (e) {
                console.error('解析JSON响应失败:', e);
                return null;
            }
        } else if (data && data.content) {
            // 尝试解析content字段（流式响应可能包含此字段）
            try {
                if (typeof data.content === 'string') {
                    const parsedContent = JSON.parse(data.content);
                    return extractNewsFromResponse(parsedContent);
                } else {
                    return extractNewsFromResponse(data.content);
                }
            } catch (e) {
                console.error('解析content字段失败:', e);
                return null;
            }
        }
        
        // 无法识别的格式，返回null
        console.warn('无法识别的数据格式:', data);
        return null;
    }

    // 确保所有新闻条目都有时间字段
    function ensureTimeField(newsArray) {
        if (!Array.isArray(newsArray)) return null;
        
        const now = new Date();
        
        return newsArray.map(item => {
            if (!item) return null;
            
            // 确保有时间字段
            if (!item.time) {
                console.log('为新闻条目添加默认时间:', item.title);
                return { ...item, time: now.toISOString() };
            }
            
            // 检查时间是否有效
            try {
                const itemDate = new Date(item.time);
                if (isNaN(itemDate.getTime())) {
                    console.log('新闻条目时间无效，重置为当前时间:', item.title);
                    return { ...item, time: now.toISOString() };
                }
            } catch (e) {
                console.error('解析时间出错，重置为当前时间:', e);
                return { ...item, time: now.toISOString() };
            }
            
            return item;
        }).filter(item => item !== null);
    }

    // 处理API响应数据
    function handleApiResponse(data) {
        console.log('处理API响应数据:', data);
        
        // 尝试从响应中提取新闻数据
        const newsData = extractNewsFromResponse(data);
        
        if (newsData && Array.isArray(newsData) && newsData.length > 0) {
            // 处理每条新闻，确保时间字段存在
            const processedNewsData = newsData.map(item => {
                // 确保每个条目都是对象
                if (!item || typeof item !== 'object') {
                    console.warn('无效的新闻条目:', item);
                    return null;
                }
                
                // 确保时间字段存在，不存在则添加当前时间
                const timeValue = item.time || new Date().toISOString();
                
                return {
                    ...item,
                    time: timeValue
                };
            }).filter(item => item !== null); // 过滤掉无效条目
            
            // 成功提取到新闻数据，使用卡片式布局渲染
            renderNewsCards(processedNewsData);
        } else {
            // 无法提取新闻数据，使用模拟数据
            console.warn('无法从响应中提取新闻数据，使用模拟数据');
            renderNewsCards(createMockNewsData('无法获取真实数据，显示模拟新闻'));
        }
    }
    
    // 将API暴露到全局，便于调试
    window.newsApi = {
        fetchNews: fetchNews,
        renderNewsCards: renderNewsCards,
        createMockNewsData: createMockNewsData,
        handleApiResponse: handleApiResponse,
        fixChineseText: fixChineseText,
        pageState: PAGE_STATE
    };
})(); 