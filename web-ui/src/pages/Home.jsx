import React from "react";
import Nav from "../components/Nav";
import Hero from "../components/Hero";
import FirstSection from "../components/FirstSection";
import SecondSection from "../components/SecondSection";
import Footer from "../components/Footer";
import Testimonial from "../components/Testimonial";
import Faq from "../components/Faq";
 
const Home = () => {
   return (
       <div>
           <Nav />
           <Hero />
           <Footer />
       </div>
   );
};
 
export default Home;