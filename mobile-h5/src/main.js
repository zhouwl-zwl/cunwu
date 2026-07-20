import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'vant/lib/index.css'
import './styles/index.scss'

import {
  NavBar,
  Tabs,
  Tab,
  Dialog,
  Form,
  Field,
  Button,
  Icon,
  Rate,
  ImagePreview,
  CellGroup,
  Picker,
  Uploader,
  Checkbox,
  CheckboxGroup,
  Cell,
  Switch,
  Stepper,
  Popup,
  DatePicker,
  Search,
  Pagination,
  Tag,
  PullRefresh,
  List,
  Skeleton,
  Steps,
  Step,
  showToast,
  showDialog
} from 'vant'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

app.use(NavBar)
app.use(Tabs)
app.use(Tab)
app.use(Dialog)
app.use(Form)
app.use(Field)
app.use(Button)
app.use(Icon)
app.use(Rate)
app.use(ImagePreview)
app.use(CellGroup)
app.use(Picker)
app.use(Uploader)
app.use(Checkbox)
app.use(CheckboxGroup)
app.use(Cell)
app.use(Switch)
app.use(Stepper)
app.use(Popup)
app.use(DatePicker)
app.use(Search)
app.use(Pagination)
app.use(Tag)
app.use(PullRefresh)
app.use(List)
app.use(Skeleton)
app.use(Steps)
app.use(Step)

app.mount('#app')

export { showToast, showDialog }
