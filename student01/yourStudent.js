function learnGitHub() {
    let successRate = Math.random() > 0.5; 
    
    if (successRate) {
        console.log(" GitHub learned! Time to push some code! ");
    } else {
        console.log(" GitHub not learned... maybe I should watch a tutorial or ask you again... ");
        setTimeout(learnGitHub, 3000); 
    }
}
console.log(" Learning GitHub begins...");
learnGitHub();
