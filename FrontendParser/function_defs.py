import locators

def get_price(driver, url):

    driver.get(url)
    if 'https://www.bestbuy.com' in url:
        return get_bestbuy_item_price(driver)
    elif 'https://www.amazon.com' in url:
        return get_amazon_item_price(driver)
    else:
        return 'site not supported'

def get_bestbuy_item_price(driver):
    price_tag_locator = driver.find_element_by_xpath(locators.bestbuy['price_tag_locator'])
    return price_tag_locator.get_attribute(locators.bestbuy['price_attribute'])

def get_amazon_item_price(driver):
    price_tag_locator = driver.find_element_by_xpath(locators.amazon['price_tag_locator'])
    return (price_tag_locator.text).replace('$', '')
