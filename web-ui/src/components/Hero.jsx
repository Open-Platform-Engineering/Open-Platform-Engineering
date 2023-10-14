import { Button, Typography } from "@material-tailwind/react";
import React from "react";
 
import hero from "../../public/download.jpeg";
 
const Hero = () => {
   return (
       <div className='w-full lg:p-8 px-4 flex ...(truncated)'>
           <div className='lg:w-[60%] w-full lg:px-6 lg:pr-14'>
               <Typography className='text-3xl ...(truncated)'>
                   Create a great circle of friends
               </Typography>
               <Typography className='font-poppins mb-6'>
                   Lorem ipsum dolor sit amet, consectetur adipiscing elit.
               </Typography>
               <Button size='lg' color='purple'>
                   Get Connected
               </Button>
           </div>
           <div className='lg:w-[40%] w-full lg:block hidden '>
               <img src={hero} alt='Hero' />
           </div>
       </div>
   );
};
 
export default Hero;