package com.greatlearning.customerDatabase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greatlearning.customerDatabase.entity.Customer;
import com.greatlearning.customerDatabase.service.CustomerService;

@Controller
@RequestMapping("/customers")
public class CustomersController {

	@Autowired
	private CustomerService customerService;

	// add mapping for "/list"

	@RequestMapping("/list")
	public String listCustomers(Model theModel) {

		// get Books from db
		List<Customer> theCustomers = customerService.findAll();

		// add to the spring model
		theModel.addAttribute("Customer", theCustomers);

		return "list-Customers";
	}

	@RequestMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		Customer theCustomer = new Customer();

		theModel.addAttribute("Customer", theCustomer);

		return "Customer-form";
	}

	@RequestMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int theId, Model theModel) {

		// get the Book from the service
		Customer theCustomer = customerService.findById(theId);

		// set Book as a model attribute to pre-populate the form
		theModel.addAttribute("Customer", theCustomer);

		// send over to our form
		return "Customer-form";
	}

	@PostMapping("/save")
	public String saveBook(@RequestParam("id") int id, @RequestParam("firstname") String firstname,
			@RequestParam("lastname") String lastname, @RequestParam("email") String email) {

		System.out.println(id);
		Customer theCustomer;
		if (id != 0) {
			theCustomer = customerService.findById(id);
			theCustomer.setfirstname(firstname);
			theCustomer.setlastname(lastname);
			theCustomer.setemail(email);
		} else
			theCustomer = new Customer(firstname, lastname, email);
		// save the Book
		customerService.save(theCustomer);

		// use a redirect to prevent duplicate submissions
		return "redirect:/customers/list";

	}

	@RequestMapping("/delete")
	public String delete(@RequestParam("customerId") int theId) {

		// delete the Book
		customerService.deleteById(theId);

		// redirect to /Books/list
		return "redirect:/customers/list";

	}

	@RequestMapping("/search")
	public String search(@RequestParam("firstname") String firstname , @RequestParam("lastname") String lastname, Model theModel) {

		// check names, if both are empty then just give list of all Books

		if (firstname.trim().isEmpty() && lastname.trim().isEmpty()) {
			return "redirect:/customers/list";
		} else {
			// else, search by first name and last name
			List<Customer> theCustomers = customerService.searchBy(firstname, lastname);

			// add to the spring model
			theModel.addAttribute("Customer", theCustomers);

			// send to list-Books
			return "list-Customers";
		}

	}
}
