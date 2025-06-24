#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

module.exports = function(context) {
    const platformRoot = path.join(context.opts.projectRoot, 'platforms', 'android');
    const buildGradlePath = path.join(platformRoot, 'build.gradle');
    
    if (!fs.existsSync(buildGradlePath)) {
        console.log('build.gradle not found, skipping Google Maven repository addition');
        return;
    }
    
    let buildGradleContent = fs.readFileSync(buildGradlePath, 'utf8');
    
    // Check if Google Maven repository is already added
    if (buildGradleContent.includes('google()')) {
        console.log('Google Maven repository already exists in build.gradle');
        return;
    }
    
    // Add Google Maven repository to allprojects repositories
    const allprojectsPattern = /allprojects\s*\{\s*repositories\s*\{/;
    if (allprojectsPattern.test(buildGradleContent)) {
        buildGradleContent = buildGradleContent.replace(
            /(allprojects\s*\{\s*repositories\s*\{)/,
            '$1\n        google()'
        );
        fs.writeFileSync(buildGradlePath, buildGradleContent);
        console.log('Added Google Maven repository to allprojects repositories');
    } else {
        console.log('Could not find allprojects repositories section in build.gradle');
    }
}; 