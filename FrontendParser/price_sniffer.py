from selenium import webdriver
from function_defs import *
import locators

# driver = webdriver.Chrome("/Users/markfavis/Projects/MarkPyProjects/rss/chromedriver")
driver = webdriver.Remote(
   command_executor='http://192.168.3.240:4444/wd/hub',
   desired_capabilities={'browserName': 'chrome', 'javascriptEnabled': True})

for key, value in locators.products.items():
   print(get_price(driver, value))

driver.quit()


