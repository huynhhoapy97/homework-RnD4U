package vn.com.hoatruong

// ---------------------------
//
// HOMEWORK
//
// You can use either Groovy or Java.
//
// Design a routine that will calculate the average Product price per Group.
//
// The Price of each Product is calculated as:
// Cost * (1 + Margin)
//
// Assume there can be a large number of products and a large number of categories.
//
// Plus points:
// - use Groovy and its closures
// - make the category look-up performance effective
// - use method Collection.inject
 
// contains information about [Product, Group, Cost]
def products = [
	["A", "G1", 20.1],
	["B", "G2", 98.4],
	["C", "G1", 49.7],
	["D", "G3", 35.8],
	["E", "G3", 105.5],
	["F", "G1", 55.2],
	["G", "G1", 12.7],
	["H", "G3", 88.6],
	["I", "G1", 5.2],
	["J", "G2", 72.4]]
 
// contains information about Category classification based on product Cost
// [Category, Cost range from (inclusive), Cost range to (exclusive)]
// i.e. if a Product has Cost between 0 and 25, it belongs to category C1
def categories = [
	["C3", 50, 75],
	["C4", 75, 100],
	["C2", 25, 50],
	["C5", 100, null],
	["C1", 0, 25]]
 
// contains information about margins for each product Category
// [Category, Margin (either percentage or absolute value)]
def margins = [
	"C1" : "20%",
	"C2" : "30%",
	"C3" : "0.4",
	"C4" : "50%",
	"C5" : "0.6"]
 
// ---------------------------
//
// YOUR CODE GOES BELOW THIS LINE
//
// Assign the 'result' variable so the assertion at the end validates
//
// ---------------------------
 
def checkNullRangeFrom = { rangeFrom ->
	if (rangeFrom == null) {
		return Integer.MIN_VALUE
	}
	
	return rangeFrom
}

def checkNullRangeTo = { rangeTo ->
	if (rangeTo == null) {
		return Integer.MAX_VALUE
	}
	
	return rangeTo
}

def changeMarginPercentToAbsoluteValue = { margin ->
	if (margin.contains("%")) {
		def percent = Integer.valueOf(margin.split("%")[0])
		margin = percent / 100
	}
	
	return margin
}

def calculatePriceOfProduct = { cost, margin ->
	def priceOfProduct = cost * (1 + margin)
	
	return priceOfProduct
}

def calculateAveragePriceOfProductByGroup = { group, listProductPerGroup ->
	def average = 0
	def totalPriceOfProduct = 0
	def totalProductInGroup = 0
	
	listProductPerGroup.each { product ->
		def groupOfProduct = product.get(1)
		def priceOfProduct = product.get(5)
		
		if (groupOfProduct.equals(group)) {
			totalPriceOfProduct += priceOfProduct
			totalProductInGroup++;
		}
	}
	
	average = (totalPriceOfProduct / totalProductInGroup).round(1)
	
	return average
}

def listProductPerGroup = categories.inject(products) { result, category ->
	result.each { product ->
		def rangeFrom = checkNullRangeFrom(category.get(1))
		def rangeTo = checkNullRangeTo(category.get(2))
		
		def cost = product.get(2);
		
		if (cost >= rangeFrom && cost <= rangeTo) {
			product.add(category.get(0))
		}
	}
}

listProductPerGroup = margins.inject(listProductPerGroup) { result, key, value ->
	result.each { product ->
		def category = key;
		def margin = value;
		
		if (product.contains(key)) {
			margin = changeMarginPercentToAbsoluteValue(margin)
			
			product.add(margin)
		}
	}
}

listProductPerGroup.each { product ->
	def cost = product.get(2)
	def margin = Double.valueOf(product.get(4));
	def priceOfProduct = calculatePriceOfProduct(cost, margin)
	
	product.add(priceOfProduct)
}

def result = [:]

listProductPerGroup.each { product ->
	def group = product.get(1)
	def average = 0
	
	if (result.size() == 0) {
		average = calculateAveragePriceOfProductByGroup(group, listProductPerGroup)
		
		result.put(group, average)
	}
	else {
		if (!result.containsKey(group)) {
			average = calculateAveragePriceOfProductByGroup(group, listProductPerGroup)
			
			result.put(group, average)
		}
	}
}

println result
 
// ---------------------------
//
// IF YOUR CODE WORKS, YOU SHOULD GET "It works!" WRITTEN IN THE CONSOLE
//
// ---------------------------
assert result == [
	"G1" : 37.5,
	"G2" : 124.5,
	"G3" : 116.1
	] : "It doesn't work"
 
println "It works!"
