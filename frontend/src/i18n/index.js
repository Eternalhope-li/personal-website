const messages = {
  zh: {
    nav:{home:'首页',about:'关于',projects:'项目',blog:'博客',contact:'联系',more:'更多',timeline:'时间轴',resume:'简历',friends:'友链',guestbook:'留言板',login:'登录',register:'注册',logout:'退出',admin:'管理后台'},
    hero:{badge:'待业中，欢迎联系',greeting:'你好，我是',name:'',creative:'创意开发者',desc:'用洁静的代码和精心的设计，构建优雅的数字体验。',viewWork:'查看作品',getInTouch:'联系我',scroll:'向下滚动'},
    section:{about:'关于',skills:'技能',projects:'项目',blog:'博客',contact:'联系',links:'导航'},
    about:{title:'关于我',story:'代码背后<br>的故事',expertise:'专业能力',doBest:'我的专长',backend:'后端开发',frontend:'前端设计',cloud:'云架构',backendDesc:'Spring Boot + JPA',frontendDesc:'Vue.js 响应式界面',cloudDesc:'Docker & Kubernetes',ctaTitle:'合作共赢',ctaDesc:'有项目想法？我很乐意了解。',ctaButton:'联系我',yearsLabel:'年经验',projectsLabel:'项目',clientsLabel:'客户'},
    skills:{title:'技术栈'},
    projects:{title:'精选作品',subtitle:'精选的项目，展示我的技能与热情。',viewAll:'查看全部项目',demo:'在线演示',source:'源代码',ctaTitle:'有项目想法吗？',ctaDesc:'让我们一起实现。',ctaButton:'发起项目'},
    blog:{title:'最新文章',subtitle:'关于开发、设计和技术的思考。',readMore:'阅读全文',readAll:'阅读更多',insights:'思考与见解',insightsSubtitle:'探索技术、设计和构建软件的艺术。'},
    contact:{title:'联系我',subtitle:'有问题、项目想法或只想打招呼？我很乐意与你聊聊。',name:'姓名 *',namePH:'您的姓名',email:'邮箱 *',emailPH:'您的邮箱',subject:'主题',subjectPH:'请问是关于什么的？',message:'留言 *',messagePH:'告诉我您的项目想法...',send:'发送消息',success:'谢谢！您的留言已收到。',error:'出错了，请稍后再试。'},
    footer:{copyright:'© 2026 电饭煲. 用 Spring Boot + Vue 搭建。'},
    user:{login:'登录',register:'注册',logout:'退出登录',loginTitle:'欢迎回来！',registerTitle:'加入我们！',username:'账号',password:'密码',usernamePH:'请输入账号',passwordPH:'请输入密码',noAccount:'没有账号？立即注册',hasAccount:'已有账号？立即登录',registered:'注册成功！请登录。',error:'用户名或密码错误',profile:'个人中心',welcome:'欢迎您！',role:'角色：',joined:'加入时间：',avatar:'头像',displayName:'昵称',displayNamePH:'请输入昵称',avatarHint:'点击上传头像',confirmPassword:'确认密码',confirmPasswordPH:'再次输入密码',captcha:'验证码',captchaPH:'输入验证码',passwordMismatch:'两次密码不一致',registerSuccess:'注册成功！您的账号是: ',accountAuto:'自动生成',save:'保存修改',saved:'保存成功！'},
    guestbook:{title:'留言板',subtitle:'留下您的留言！打招呼、分享想法。',name:'姓名 *',namePH:'您的姓名',email:'邮箱',emailPH:'您的邮箱',message:'留言 *',messagePH:'写下您的留言...',submit:'提交',messages:'留言列表',empty:'暂无留言，抢沙发！'},
    timeline:{subtitle:'我的成长历程。',empty:'暂无时间轴内容。'},
    resume:{title:'简历',subtitle:'我的专业经历。',empty:'暂无简历内容。'},
    friends:{tag:'友链',subtitle:'我推荐的朋友和网站。',empty:'暂无友链。',cta:'交换友链？',ctaDesc:'如果您也有个人网站，欢迎联系我！'},
    theme:{light:'白昼',dark:'夜晚'},
    error:{notfound:'页面不存在',notfoundDesc:'您要找的页面不存在。',servererror:'服务器内部错误',servererrorDesc:'出点问题了，请稍后再试。'},
    common:{emptyPosts:'暂无文章',emptyProjects:'暂无项目',searchHint:'按 Ctrl+K 搜索',admin:'从后台添加'}
  },
  en: {
    nav:{home:'Home',about:'About',projects:'Projects',blog:'Blog',contact:'Contact',more:'More',timeline:'Timeline',resume:'Resume',friends:'Friends',guestbook:'Guestbook',login:'Sign In',register:'Register',logout:'Logout',admin:'Admin Panel'},
    hero:{badge:'Available for opportunities',greeting:"Hi, I'm",name:'',creative:'Creative Developer',desc:'Crafting elegant digital experiences with clean code and thoughtful design.',viewWork:'View My Work',getInTouch:'Get in Touch',scroll:'Scroll Down'},
    section:{about:'About',skills:'Skills',projects:'Projects',blog:'Blog',contact:'Contact',links:'Links'},
    about:{title:'Who I Am',story:'The Story Behind<br>the Code',expertise:'Expertise',doBest:'What I Do Best',backend:'Backend',frontend:'Frontend',cloud:'Cloud',backendDesc:'Spring Boot & JPA',frontendDesc:'Vue.js Interfaces',cloudDesc:'Docker & Kubernetes',ctaTitle:"Let's Work Together",ctaDesc:'Have a project in mind?',ctaButton:'Get in Touch',yearsLabel:'Years Exp',projectsLabel:'Projects',clientsLabel:'Clients'},
    skills:{title:'Tech Stack'},
    projects:{title:'Featured Work',subtitle:'Selected projects that showcase my skills.',viewAll:'View All',demo:'Live Demo',source:'Source',ctaTitle:'Have an Idea?',ctaDesc:"Let's bring it to life.",ctaButton:'Start Project'},
    blog:{title:'Latest Posts',subtitle:'Thoughts on development, design, and technology.',readMore:'Read More',readAll:'Read All Posts',insights:'Thoughts and Insights',insightsSubtitle:'Exploring technology, design, and the art of building software.'},
    contact:{title:'Get in Touch',subtitle:"Have a question? I'd love to hear from you.",name:'Name *',namePH:'Your name',email:'Email *',emailPH:'your@email.com',subject:'Subject',subjectPH:"What's this about?",message:'Message *',messagePH:'Tell me about your project...',send:'Send Message',success:'Thank you! Your message has been received.',error:'Something went wrong.'},
    footer:{copyright:'© 2026 Rice Cooker. Built with Spring Boot + Vue.'},
    user:{login:'Sign In',register:'Register',logout:'Logout',loginTitle:'Welcome back!',registerTitle:'Join us!',username:'Username',password:'Password',usernamePH:'Enter username',passwordPH:'Enter password',noAccount:"No account? Register",hasAccount:"Already have an account? Sign in",registered:'Registration successful! Please sign in.',error:'Invalid username or password',profile:'My Profile',welcome:'Welcome!',role:'Role:',joined:'Joined:',avatar:'Avatar',displayName:'Display Name',displayNamePH:'Enter display name',avatarHint:'Click to upload avatar',confirmPassword:'Confirm Password',confirmPasswordPH:'Re-enter password',captcha:'Captcha',captchaPH:'Enter captcha',passwordMismatch:'Passwords do not match',registerSuccess:'Registered! Your account: ',accountAuto:'Auto-generated',save:'Save Changes',saved:'Saved!'},
    guestbook:{title:'Guestbook',subtitle:"Leave a message! Say hi, share your thoughts.",name:'Name *',namePH:'Your name',email:'Email',emailPH:'your@email.com',message:'Message *',messagePH:'Write your message...',submit:'Submit',messages:'Messages',empty:'No messages yet. Be the first!'},
    timeline:{subtitle:'My journey so far.',empty:'No timeline entries yet.'},
    resume:{title:'Online CV',subtitle:'My professional experience.',empty:'No resume content yet.'},
    friends:{tag:'Network',subtitle:'Awesome people and websites I recommend.',empty:'No friends yet.',cta:'Exchange Links?',ctaDesc:'Feel free to reach out!'},
    theme:{light:'Day',dark:'Night'},
    error:{notfound:'Page Not Found',notfoundDesc:'The page you are looking for does not exist.',servererror:'Server Error',servererrorDesc:'Something went wrong.'},
    common:{emptyPosts:'No posts yet',emptyProjects:'No projects yet',searchHint:'Press Ctrl+K to search',admin:'Add from admin panel'}
  }
};

function gl(){try{return localStorage.getItem('lang')}catch(e){return null}}
var cl=gl()||'zh';
export function t(p){var ks=p.split('.'),o=messages[cl];for(var k of ks){if(o&&o[k]!==undefined)o=o[k];else return p}return o}
export function setLang(l){cl=l;try{localStorage.setItem('lang',l)}catch(e){}}
export function getLang(){return cl}
