





<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">



  <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/frameworks-81a59bf26d881d29286674f6deefe779c444382fff322085b50ba455460ccae5.css" integrity="sha256-gaWb8m2IHSkoZnT23u/necREOC//MiCFtQukVUYMyuU=" media="all" rel="stylesheet" />
  <link crossorigin="anonymous" href="https://assets-cdn.github.com/assets/github-4a6d0d25a011b7202858c52c047bda6bd227d8853abb3e70edf75785f6a78122.css" integrity="sha256-Sm0NJaARtyAoWMUsBHvaa9In2IU6uz5w7fdXhfangSI=" media="all" rel="stylesheet" />
  
  
  
  

  <meta name="viewport" content="width=device-width">
  
  <title>art-template/template-web.js at master · aui/art-template</title>
  <link rel="search" type="application/opensearchdescription+xml" href="/opensearch.xml" title="GitHub">
  <link rel="fluid-icon" href="https://github.com/fluidicon.png" title="GitHub">
  <meta property="fb:app_id" content="1401488693436528">

    
    <meta content="https://avatars2.githubusercontent.com/u/1791748?v=3&amp;s=400" property="og:image" /><meta content="GitHub" property="og:site_name" /><meta content="object" property="og:type" /><meta content="aui/art-template" property="og:title" /><meta content="https://github.com/aui/art-template" property="og:url" /><meta content="art-template - 性能卓越的 js 模板引擎" property="og:description" />

  <link rel="assets" href="https://assets-cdn.github.com/">
  <link rel="web-socket" href="wss://live.github.com/_sockets/VjI6MTcxMzI4MjQzOjRmNzExNmVlNTE4MTVmZjQ1ZWU1YTI3OTFkZWYxYTNjNTQwMGMxOThlODNhOTQ2ZmQzYTI0MjMxMjA4Y2U1NTU=--8f6ec7147622fb8eb8860dd2907292130dbfdd35">
  <meta name="pjax-timeout" content="1000">
  <link rel="sudo-modal" href="/sessions/sudo_modal">
  <meta name="request-id" content="F801:28185:125F9EE:1C62B7E:590B3736" data-pjax-transient>
  

  <meta name="selected-link" value="repo_source" data-pjax-transient>

  <meta name="google-site-verification" content="KT5gs8h0wvaagLKAVWq8bbeNwnZZK1r1XQysX3xurLU">
<meta name="google-site-verification" content="ZzhVyEFwb7w3e0-uOTltm8Jsck2F5StVihD0exw2fsA">
    <meta name="google-analytics" content="UA-3769691-2">

<meta content="collector.githubapp.com" name="octolytics-host" /><meta content="github" name="octolytics-app-id" /><meta content="https://collector.githubapp.com/github-external/browser_event" name="octolytics-event-url" /><meta content="F801:28185:125F9EE:1C62B7E:590B3736" name="octolytics-dimension-request_id" /><meta content="21231746" name="octolytics-actor-id" /><meta content="SchuckBeta" name="octolytics-actor-login" /><meta content="35cc94fcf213fac4012c09b8df0024b021414b57e5bc58e350e2d4747aa588f8" name="octolytics-actor-hash" />
<meta content="/&lt;user-name&gt;/&lt;repo-name&gt;/blob/show" data-pjax-transient="true" name="analytics-location" />




  <meta class="js-ga-set" name="dimension1" content="Logged In">


  

      <meta name="hostname" content="github.com">
  <meta name="user-login" content="SchuckBeta">

      <meta name="expected-hostname" content="github.com">
    <meta name="js-proxy-site-detection-payload" content="YzFkOTYzYzUwMWUyZmFjODg4M2M5NmQyYTExZGY0YjJjNzNjNWM1MmZjYjY4Y2RlODQ3NzdkYzE3ODM0YWI5Y3x7InJlbW90ZV9hZGRyZXNzIjoiNTkuMTc1LjIzMy4xOTQiLCJyZXF1ZXN0X2lkIjoiRjgwMToyODE4NToxMjVGOUVFOjFDNjJCN0U6NTkwQjM3MzYiLCJ0aW1lc3RhbXAiOjE0OTM5MDcyNjIsImhvc3QiOiJnaXRodWIuY29tIn0=">


  <meta name="html-safe-nonce" content="868070da70e12660503c7bddcca0c0aa30cfaead">

  <meta http-equiv="x-pjax-version" content="54135a3b0eac0835751e71ad3f7d74ce">
  

    
  <meta name="description" content="art-template - 性能卓越的 js 模板引擎">
  <meta name="go-import" content="github.com/aui/art-template git https://github.com/aui/art-template.git">

  <meta content="1791748" name="octolytics-dimension-user_id" /><meta content="aui" name="octolytics-dimension-user_login" /><meta content="4534540" name="octolytics-dimension-repository_id" /><meta content="aui/art-template" name="octolytics-dimension-repository_nwo" /><meta content="true" name="octolytics-dimension-repository_public" /><meta content="false" name="octolytics-dimension-repository_is_fork" /><meta content="4534540" name="octolytics-dimension-repository_network_root_id" /><meta content="aui/art-template" name="octolytics-dimension-repository_network_root_nwo" />
  <link href="https://github.com/aui/art-template/commits/master.atom" rel="alternate" title="Recent Commits to art-template:master" type="application/atom+xml">


    <link rel="canonical" href="https://github.com/aui/art-template/blob/master/lib/template-web.js" data-pjax-transient>


  <meta name="browser-stats-url" content="https://api.github.com/_private/browser/stats">

  <meta name="browser-errors-url" content="https://api.github.com/_private/browser/errors">

  <link rel="mask-icon" href="https://assets-cdn.github.com/pinned-octocat.svg" color="#000000">
  <link rel="icon" type="image/x-icon" href="https://assets-cdn.github.com/favicon.ico">

<meta name="theme-color" content="#1e2327">



  </head>

  <body class="logged-in env-production page-blob">
    


  <div class="position-relative js-header-wrapper ">
    <a href="#start-of-content" tabindex="1" class="accessibility-aid js-skip-to-content">Skip to content</a>
    <div id="js-pjax-loader-bar" class="pjax-loader-bar"><div class="progress"></div></div>

    
    
    



        
<div class="header" role="banner">
  <div class="container clearfix">
    <a class="header-logo-invertocat" href="https://github.com/" data-hotkey="g d" aria-label="Homepage" data-ga-click="Header, go to dashboard, icon:logo">
  <svg aria-hidden="true" class="octicon octicon-mark-github" height="32" version="1.1" viewBox="0 0 16 16" width="32"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
</a>


        <div class="header-search scoped-search site-scoped-search js-site-search" role="search">
  <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/aui/art-template/search" class="js-site-search-form" data-scoped-search-url="/aui/art-template/search" data-unscoped-search-url="/search" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <label class="form-control header-search-wrapper js-chromeless-input-container">
        <a href="/aui/art-template/blob/master/lib/template-web.js" class="header-search-scope no-underline">This repository</a>
      <input type="text"
        class="form-control header-search-input js-site-search-focus js-site-search-field is-clearable"
        data-hotkey="s"
        name="q"
        value=""
        placeholder="Search"
        aria-label="Search this repository"
        data-unscoped-placeholder="Search GitHub"
        data-scoped-placeholder="Search"
        autocapitalize="off">
        <input type="hidden" class="js-site-search-type-field" name="type" >
    </label>
</form></div>


      <ul class="header-nav float-left" role="navigation">
        <li class="header-nav-item">
          <a href="/pulls" aria-label="Pull requests you created" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:pulls context:user" data-hotkey="g p" data-selected-links="/pulls /pulls/assigned /pulls/mentioned /pulls">
            Pull requests
</a>        </li>
        <li class="header-nav-item">
          <a href="/issues" aria-label="Issues you created" class="js-selected-navigation-item header-nav-link" data-ga-click="Header, click, Nav menu - item:issues context:user" data-hotkey="g i" data-selected-links="/issues /issues/assigned /issues/mentioned /issues">
            Issues
</a>        </li>
          <li class="header-nav-item">
            <a class="header-nav-link" href="https://gist.github.com/" data-ga-click="Header, go to gist, text:gist">Gist</a>
          </li>
      </ul>

    
<ul class="header-nav user-nav float-right" id="user-links">
  <li class="header-nav-item">
    

  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link tooltipped tooltipped-s js-menu-target" href="/new"
       aria-label="Create new…"
       data-ga-click="Header, create new, icon:add">
      <svg aria-hidden="true" class="octicon octicon-plus float-left" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 9H7v5H5V9H0V7h5V2h2v5h5z"/></svg>
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <ul class="dropdown-menu dropdown-menu-sw">
        
<a class="dropdown-item" href="/new" data-ga-click="Header, create new repository">
  New repository
</a>

  <a class="dropdown-item" href="/new/import" data-ga-click="Header, import a repository">
    Import repository
  </a>

<a class="dropdown-item" href="https://gist.github.com/" data-ga-click="Header, create new gist">
  New gist
</a>

  <a class="dropdown-item" href="/organizations/new" data-ga-click="Header, create new organization">
    New organization
  </a>



  <div class="dropdown-divider"></div>
  <div class="dropdown-header">
    <span title="aui/art-template">This repository</span>
  </div>
    <a class="dropdown-item" href="/aui/art-template/issues/new" data-ga-click="Header, create new issue">
      New issue
    </a>

      </ul>
    </div>
  </li>

  <li class="header-nav-item dropdown js-menu-container">
    <a class="header-nav-link name tooltipped tooltipped-sw js-menu-target" href="/SchuckBeta"
       aria-label="View profile and more"
       data-ga-click="Header, show menu, icon:avatar">
      <img alt="@SchuckBeta" class="avatar" src="https://avatars1.githubusercontent.com/u/21231746?v=3&amp;s=40" height="20" width="20">
      <span class="dropdown-caret"></span>
    </a>

    <div class="dropdown-menu-content js-menu-content">
      <div class="dropdown-menu dropdown-menu-sw">
        <div class="dropdown-header header-nav-current-user css-truncate">
          Signed in as <strong class="css-truncate-target">SchuckBeta</strong>
        </div>

        <div class="dropdown-divider"></div>

        <a class="dropdown-item" href="/SchuckBeta" data-ga-click="Header, go to profile, text:your profile">
          Your profile
        </a>
        <a class="dropdown-item" href="/SchuckBeta?tab=stars" data-ga-click="Header, go to starred repos, text:your stars">
          Your stars
        </a>
        <a class="dropdown-item" href="/explore" data-ga-click="Header, go to explore, text:explore">
          Explore
        </a>
          <a class="dropdown-item" href="/integrations" data-ga-click="Header, go to integrations, text:integrations">
            Integrations
          </a>
        <a class="dropdown-item" href="https://help.github.com" data-ga-click="Header, go to help, text:help">
          Help
        </a>

        <div class="dropdown-divider"></div>

        <a class="dropdown-item" href="/settings/profile" data-ga-click="Header, go to settings, icon:settings">
          Settings
        </a>

        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/logout" class="logout-form" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="bgdjJsbmetOjJsfM/FUZmIyv7IJ9sKr6w+hKcthvVZQV9iedujItdc19rDqsoy/kxr7rCVupyRxaOx5Wf9FHuw==" /></div>
          <button type="submit" class="dropdown-item dropdown-signout" data-ga-click="Header, sign out, icon:logout">
            Sign out
          </button>
</form>      </div>
    </div>
  </li>
</ul>


    <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/logout" class="sr-only right-0" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="7k4JSoyCaGDRn6jdCtXs+I/bko9WdBJj9SeuD8AN5UyVv03x8FY/xr/EwytaI9qExcqVBHBtcYVs9PorZ7P3Yw==" /></div>
      <button type="submit" class="dropdown-item dropdown-signout" data-ga-click="Header, sign out, icon:logout">
        Sign out
      </button>
</form>  </div>
</div>


      

  </div>

  <div id="start-of-content" class="accessibility-aid"></div>

    <div id="js-flash-container">
</div>



  <div role="main">
        <div itemscope itemtype="http://schema.org/SoftwareSourceCode">
    <div id="js-repo-pjax-container" data-pjax-container>
        



    <div class="pagehead repohead instapaper_ignore readability-menu experiment-repo-nav">
      <div class="container repohead-details-container">

        <ul class="pagehead-actions">
  <li>
        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/notifications/subscribe" class="js-social-container" data-autosubmit="true" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="c+yGivgTD17zY83knTYxKhiaIKPNE2AGxjjqrqTS7Gid7DgaK7CmVsfJUKYFDL1U4bpcwjIOc9BNE8Y0+frZzA==" /></div>      <input class="form-control" id="repository_id" name="repository_id" type="hidden" value="4534540" />

        <div class="select-menu js-menu-container js-select-menu">
          <a href="/aui/art-template/subscription"
            class="btn btn-sm btn-with-count select-menu-button js-menu-target" role="button" tabindex="0" aria-haspopup="true"
            data-ga-click="Repository, click Watch settings, action:blob#show">
            <span class="js-select-button">
                <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
                Watch
            </span>
          </a>
            <a class="social-count js-social-count"
              href="/aui/art-template/watchers"
              aria-label="427 users are watching this repository">
              427
            </a>

        <div class="select-menu-modal-holder">
          <div class="select-menu-modal subscription-menu-modal js-menu-content">
            <div class="select-menu-header js-navigation-enable" tabindex="-1">
              <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
              <span class="select-menu-title">Notifications</span>
            </div>

              <div class="select-menu-list js-navigation-container" role="menu">

                <div class="select-menu-item js-navigation-item selected" role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
                  <div class="select-menu-item-text">
                    <input checked="checked" id="do_included" name="do" type="radio" value="included" />
                    <span class="select-menu-item-heading">Not watching</span>
                    <span class="description">Be notified when participating or @mentioned.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
                      Watch
                    </span>
                  </div>
                </div>

                <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
                  <div class="select-menu-item-text">
                    <input id="do_subscribed" name="do" type="radio" value="subscribed" />
                    <span class="select-menu-item-heading">Watching</span>
                    <span class="description">Be notified of all conversations.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-eye" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.06 2C3 2 0 8 0 8s3 6 8.06 6C13 14 16 8 16 8s-3-6-7.94-6zM8 12c-2.2 0-4-1.78-4-4 0-2.2 1.8-4 4-4 2.22 0 4 1.8 4 4 0 2.22-1.78 4-4 4zm2-4c0 1.11-.89 2-2 2-1.11 0-2-.89-2-2 0-1.11.89-2 2-2 1.11 0 2 .89 2 2z"/></svg>
                        Unwatch
                    </span>
                  </div>
                </div>

                <div class="select-menu-item js-navigation-item " role="menuitem" tabindex="0">
                  <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
                  <div class="select-menu-item-text">
                    <input id="do_ignore" name="do" type="radio" value="ignore" />
                    <span class="select-menu-item-heading">Ignoring</span>
                    <span class="description">Never be notified.</span>
                    <span class="js-select-button-text hidden-select-button-text">
                      <svg aria-hidden="true" class="octicon octicon-mute" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8 2.81v10.38c0 .67-.81 1-1.28.53L3 10H1c-.55 0-1-.45-1-1V7c0-.55.45-1 1-1h2l3.72-3.72C7.19 1.81 8 2.14 8 2.81zm7.53 3.22l-1.06-1.06-1.97 1.97-1.97-1.97-1.06 1.06L11.44 8 9.47 9.97l1.06 1.06 1.97-1.97 1.97 1.97 1.06-1.06L13.56 8l1.97-1.97z"/></svg>
                        Stop ignoring
                    </span>
                  </div>
                </div>

              </div>

            </div>
          </div>
        </div>
</form>
  </li>

  <li>
      <div class="js-toggler-container js-social-container starring-container ">
    <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/aui/art-template/unstar" class="starred" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="Z8oTRZUYQsL4Oe212ctsIZ2EUoavTv/SudTkc+QAlaFQl3zLmEP4ybwzNXktzejCNWRyyoiBKHm3XJijUhVarg==" /></div>
      <button
        type="submit"
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Unstar this repository" title="Unstar aui/art-template"
        data-ga-click="Repository, click unstar button, action:blob#show; text:Unstar">
        <svg aria-hidden="true" class="octicon octicon-star" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M14 6l-4.9-.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14 7 11.67 11.33 14l-.93-4.74z"/></svg>
        Unstar
      </button>
        <a class="social-count js-social-count" href="/aui/art-template/stargazers"
           aria-label="4806 users starred this repository">
          4,806
        </a>
</form>
    <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/aui/art-template/star" class="unstarred" data-remote="true" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="/s2VAXLyPj5LmXBT/+AYp1cRKmGaD0hOFBHd4Ctolu8kzguAJaEoZxIjVytUpPLCQKOm7ZfYpE4hSnxyjZb46g==" /></div>
      <button
        type="submit"
        class="btn btn-sm btn-with-count js-toggler-target"
        aria-label="Star this repository" title="Star aui/art-template"
        data-ga-click="Repository, click star button, action:blob#show; text:Star">
        <svg aria-hidden="true" class="octicon octicon-star" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M14 6l-4.9-.64L7 1 4.9 5.36 0 6l3.6 3.26L2.67 14 7 11.67 11.33 14l-.93-4.74z"/></svg>
        Star
      </button>
        <a class="social-count js-social-count" href="/aui/art-template/stargazers"
           aria-label="4806 users starred this repository">
          4,806
        </a>
</form>  </div>

  </li>

  <li>
          <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/aui/art-template/fork" class="btn-with-count" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="m2Nx1FrdUvbV+gcefwUc9cNhq+BockgWsF3QbZDIV5j7409e86+Z6X93bAI7yl9bbITaMy9R11PFgCIgjg3CjQ==" /></div>
            <button
                type="submit"
                class="btn btn-sm btn-with-count"
                data-ga-click="Repository, show fork modal, action:blob#show; text:Fork"
                title="Fork your own copy of aui/art-template to your account"
                aria-label="Fork your own copy of aui/art-template to your account">
              <svg aria-hidden="true" class="octicon octicon-repo-forked" height="16" version="1.1" viewBox="0 0 10 16" width="10"><path fill-rule="evenodd" d="M8 1a1.993 1.993 0 0 0-1 3.72V6L5 8 3 6V4.72A1.993 1.993 0 0 0 2 1a1.993 1.993 0 0 0-1 3.72V6.5l3 3v1.78A1.993 1.993 0 0 0 5 15a1.993 1.993 0 0 0 1-3.72V9.5l3-3V4.72A1.993 1.993 0 0 0 8 1zM2 4.2C1.34 4.2.8 3.65.8 3c0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zm3 10c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zm3-10c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2z"/></svg>
              Fork
            </button>
</form>
    <a href="/aui/art-template/network" class="social-count"
       aria-label="1712 users forked this repository">
      1,712
    </a>
  </li>
</ul>

        <h1 class="public ">
  <svg aria-hidden="true" class="octicon octicon-repo" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M4 9H3V8h1v1zm0-3H3v1h1V6zm0-2H3v1h1V4zm0-2H3v1h1V2zm8-1v12c0 .55-.45 1-1 1H6v2l-1.5-1.5L3 16v-2H1c-.55 0-1-.45-1-1V1c0-.55.45-1 1-1h10c.55 0 1 .45 1 1zm-1 10H1v2h2v-1h3v1h5v-2zm0-10H2v9h9V1z"/></svg>
  <span class="author" itemprop="author"><a href="/aui" class="url fn" rel="author">aui</a></span><!--
--><span class="path-divider">/</span><!--
--><strong itemprop="name"><a href="/aui/art-template" data-pjax="#js-repo-pjax-container">art-template</a></strong>

</h1>

      </div>
      <div class="container">
        
<nav class="reponav js-repo-nav js-sidenav-container-pjax"
     itemscope
     itemtype="http://schema.org/BreadcrumbList"
     role="navigation"
     data-pjax="#js-repo-pjax-container">

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/aui/art-template" class="js-selected-navigation-item selected reponav-item" data-hotkey="g c" data-selected-links="repo_source repo_downloads repo_commits repo_releases repo_tags repo_branches /aui/art-template" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-code" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M9.5 3L8 4.5 11.5 8 8 11.5 9.5 13 14 8 9.5 3zm-5 0L0 8l4.5 5L6 11.5 2.5 8 6 4.5 4.5 3z"/></svg>
      <span itemprop="name">Code</span>
      <meta itemprop="position" content="1">
</a>  </span>

    <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
      <a href="/aui/art-template/issues" class="js-selected-navigation-item reponav-item" data-hotkey="g i" data-selected-links="repo_issues repo_labels repo_milestones /aui/art-template/issues" itemprop="url">
        <svg aria-hidden="true" class="octicon octicon-issue-opened" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M7 2.3c3.14 0 5.7 2.56 5.7 5.7s-2.56 5.7-5.7 5.7A5.71 5.71 0 0 1 1.3 8c0-3.14 2.56-5.7 5.7-5.7zM7 1C3.14 1 0 4.14 0 8s3.14 7 7 7 7-3.14 7-7-3.14-7-7-7zm1 3H6v5h2V4zm0 6H6v2h2v-2z"/></svg>
        <span itemprop="name">Issues</span>
        <span class="Counter">119</span>
        <meta itemprop="position" content="2">
</a>    </span>

  <span itemscope itemtype="http://schema.org/ListItem" itemprop="itemListElement">
    <a href="/aui/art-template/pulls" class="js-selected-navigation-item reponav-item" data-hotkey="g p" data-selected-links="repo_pulls /aui/art-template/pulls" itemprop="url">
      <svg aria-hidden="true" class="octicon octicon-git-pull-request" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M11 11.28V5c-.03-.78-.34-1.47-.94-2.06C9.46 2.35 8.78 2.03 8 2H7V0L4 3l3 3V4h1c.27.02.48.11.69.31.21.2.3.42.31.69v6.28A1.993 1.993 0 0 0 10 15a1.993 1.993 0 0 0 1-3.72zm-1 2.92c-.66 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2zM4 3c0-1.11-.89-2-2-2a1.993 1.993 0 0 0-1 3.72v6.56A1.993 1.993 0 0 0 2 15a1.993 1.993 0 0 0 1-3.72V4.72c.59-.34 1-.98 1-1.72zm-.8 10c0 .66-.55 1.2-1.2 1.2-.65 0-1.2-.55-1.2-1.2 0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2zM2 4.2C1.34 4.2.8 3.65.8 3c0-.65.55-1.2 1.2-1.2.65 0 1.2.55 1.2 1.2 0 .65-.55 1.2-1.2 1.2z"/></svg>
      <span itemprop="name">Pull requests</span>
      <span class="Counter">0</span>
      <meta itemprop="position" content="3">
</a>  </span>

    <a href="/aui/art-template/projects" class="js-selected-navigation-item reponav-item" data-selected-links="repo_projects new_repo_project repo_project /aui/art-template/projects">
      <svg aria-hidden="true" class="octicon octicon-project" height="16" version="1.1" viewBox="0 0 15 16" width="15"><path fill-rule="evenodd" d="M10 12h3V2h-3v10zm-4-2h3V2H6v8zm-4 4h3V2H2v12zm-1 1h13V1H1v14zM14 0H1a1 1 0 0 0-1 1v14a1 1 0 0 0 1 1h13a1 1 0 0 0 1-1V1a1 1 0 0 0-1-1z"/></svg>
      Projects
      <span class="Counter" >0</span>
</a>
    <a href="/aui/art-template/wiki" class="js-selected-navigation-item reponav-item" data-hotkey="g w" data-selected-links="repo_wiki /aui/art-template/wiki">
      <svg aria-hidden="true" class="octicon octicon-book" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M3 5h4v1H3V5zm0 3h4V7H3v1zm0 2h4V9H3v1zm11-5h-4v1h4V5zm0 2h-4v1h4V7zm0 2h-4v1h4V9zm2-6v9c0 .55-.45 1-1 1H9.5l-1 1-1-1H2c-.55 0-1-.45-1-1V3c0-.55.45-1 1-1h5.5l1 1 1-1H15c.55 0 1 .45 1 1zm-8 .5L7.5 3H2v9h6V3.5zm7-.5H9.5l-.5.5V12h6V3z"/></svg>
      Wiki
</a>

  <a href="/aui/art-template/pulse" class="js-selected-navigation-item reponav-item" data-selected-links="pulse /aui/art-template/pulse">
    <svg aria-hidden="true" class="octicon octicon-pulse" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M11.5 8L8.8 5.4 6.6 8.5 5.5 1.6 2.38 8H0v2h3.6l.9-1.8.9 5.4L9 8.5l1.6 1.5H14V8z"/></svg>
    Pulse
</a>
  <a href="/aui/art-template/graphs" class="js-selected-navigation-item reponav-item" data-selected-links="repo_graphs repo_contributors /aui/art-template/graphs">
    <svg aria-hidden="true" class="octicon octicon-graph" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M16 14v1H0V0h1v14h15zM5 13H3V8h2v5zm4 0H7V3h2v10zm4 0h-2V6h2v7z"/></svg>
    Graphs
</a>

</nav>

      </div>
    </div>

<div class="container new-discussion-timeline experiment-repo-nav">
  <div class="repository-content">

    
          

<a href="/aui/art-template/blob/7f1eaf997b1abaa032248d7f595cd8328d3fa3fd/lib/template-web.js" class="d-none js-permalink-shortcut" data-hotkey="y">Permalink</a>

<!-- blob contrib key: blob_contributors:v21:e5a071fc3168ccf5cbf31d86878b7036 -->

<div class="file-navigation js-zeroclipboard-container">
  
<div class="select-menu branch-select-menu js-menu-container js-select-menu float-left">
  <button class=" btn btn-sm select-menu-button js-menu-target css-truncate" data-hotkey="w"
    
    type="button" aria-label="Switch branches or tags" tabindex="0" aria-haspopup="true">
      <i>Branch:</i>
      <span class="js-select-button css-truncate-target">master</span>
  </button>

  <div class="select-menu-modal-holder js-menu-content js-navigation-container" data-pjax>

    <div class="select-menu-modal">
      <div class="select-menu-header">
        <svg aria-label="Close" class="octicon octicon-x js-menu-close" height="16" role="img" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
        <span class="select-menu-title">Switch branches/tags</span>
      </div>

      <div class="select-menu-filters">
        <div class="select-menu-text-filter">
          <input type="text" aria-label="Filter branches/tags" id="context-commitish-filter-field" class="form-control js-filterable-field js-navigation-enable" placeholder="Filter branches/tags">
        </div>
        <div class="select-menu-tabs">
          <ul>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="branches" data-filter-placeholder="Filter branches/tags" class="js-select-menu-tab" role="tab">Branches</a>
            </li>
            <li class="select-menu-tab">
              <a href="#" data-tab-filter="tags" data-filter-placeholder="Find a tag…" class="js-select-menu-tab" role="tab">Tags</a>
            </li>
          </ul>
        </div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="branches" role="menu">

        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/1.4.0/lib/template-web.js"
               data-name="1.4.0"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                1.4.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/2.0.1/lib/template-web.js"
               data-name="2.0.1"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                2.0.1
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/2.0.2/lib/template-web.js"
               data-name="2.0.2"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                2.0.2
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/2.0.3/lib/template-web.js"
               data-name="2.0.3"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                2.0.3
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/2.0.4/lib/template-web.js"
               data-name="2.0.4"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                2.0.4
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/2.1.0(Beta)/lib/template-web.js"
               data-name="2.1.0(Beta)"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                2.1.0(Beta)
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/3.0.0/lib/template-web.js"
               data-name="3.0.0"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                3.0.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/3.1.0/lib/template-web.js"
               data-name="3.1.0"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                3.1.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/4.0.0/lib/template-web.js"
               data-name="4.0.0"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                4.0.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
               href="/aui/art-template/blob/gh-pages/lib/template-web.js"
               data-name="gh-pages"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                gh-pages
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open selected"
               href="/aui/art-template/blob/master/lib/template-web.js"
               data-name="master"
               data-skip-pjax="true"
               rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target js-select-menu-filter-text">
                master
              </span>
            </a>
        </div>

          <div class="select-menu-no-results">Nothing to show</div>
      </div>

      <div class="select-menu-list select-menu-tab-bucket js-select-menu-tab-bucket" data-tab-filter="tags">
        <div data-filterable-for="context-commitish-filter-field" data-filterable-type="substring">


            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.8.1/lib/template-web.js"
              data-name="v4.8.1"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.8.1">
                v4.8.1
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.8.0/lib/template-web.js"
              data-name="v4.8.0"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.8.0">
                v4.8.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.7.0/lib/template-web.js"
              data-name="v4.7.0"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.7.0">
                v4.7.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.6.0/lib/template-web.js"
              data-name="v4.6.0"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.6.0">
                v4.6.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.5.1/lib/template-web.js"
              data-name="v4.5.1"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.5.1">
                v4.5.1
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.5.0/lib/template-web.js"
              data-name="v4.5.0"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.5.0">
                v4.5.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.4.2/lib/template-web.js"
              data-name="v4.4.2"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.4.2">
                v4.4.2
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.4.1/lib/template-web.js"
              data-name="v4.4.1"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.4.1">
                v4.4.1
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.1.0/lib/template-web.js"
              data-name="v4.1.0"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.1.0">
                v4.1.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v4.0.0/lib/template-web.js"
              data-name="v4.0.0"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v4.0.0">
                v4.0.0
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/v3.1.3/lib/template-web.js"
              data-name="v3.1.3"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="v3.1.3">
                v3.1.3
              </span>
            </a>
            <a class="select-menu-item js-navigation-item js-navigation-open "
              href="/aui/art-template/tree/3.0.1/lib/template-web.js"
              data-name="3.0.1"
              data-skip-pjax="true"
              rel="nofollow">
              <svg aria-hidden="true" class="octicon octicon-check select-menu-item-icon" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M12 5l-8 8-4-4 1.5-1.5L4 10l6.5-6.5z"/></svg>
              <span class="select-menu-item-text css-truncate-target" title="3.0.1">
                3.0.1
              </span>
            </a>
        </div>

        <div class="select-menu-no-results">Nothing to show</div>
      </div>

    </div>
  </div>
</div>

  <div class="BtnGroup float-right">
    <a href="/aui/art-template/find/master"
          class="js-pjax-capture-input btn btn-sm BtnGroup-item"
          data-pjax
          data-hotkey="t">
      Find file
    </a>
    <button aria-label="Copy file path to clipboard" class="js-zeroclipboard btn btn-sm BtnGroup-item tooltipped tooltipped-s" data-copied-hint="Copied!" type="button">Copy path</button>
  </div>
  <div class="breadcrumb js-zeroclipboard-target">
    <span class="repo-root js-repo-root"><span class="js-path-segment"><a href="/aui/art-template"><span>art-template</span></a></span></span><span class="separator">/</span><span class="js-path-segment"><a href="/aui/art-template/tree/master/lib"><span>lib</span></a></span><span class="separator">/</span><strong class="final-path">template-web.js</strong>
  </div>
</div>



  <div class="commit-tease">
      <span class="float-right">
        <a class="commit-tease-sha" href="/aui/art-template/commit/936d9f372d51a2221b351b2a62847454dd27d55e" data-pjax>
          936d9f3
        </a>
        <relative-time datetime="2017-05-04T09:49:48Z">May 4, 2017</relative-time>
      </span>
      <div>
        <img alt="@aui" class="avatar" height="20" src="https://avatars0.githubusercontent.com/u/1791748?v=3&amp;s=40" width="20" />
        <a href="/aui" class="user-mention" rel="author">aui</a>
          <a href="/aui/art-template/commit/936d9f372d51a2221b351b2a62847454dd27d55e" class="message" data-pjax="true" title="v4.8.1">v4.8.1</a>
      </div>

    <div class="commit-tease-contributors">
      <button type="button" class="btn-link muted-link contributors-toggle" data-facebox="#blob_contributors_box">
        <strong>1</strong>
         contributor
      </button>
      
    </div>

    <div id="blob_contributors_box" style="display:none">
      <h2 class="facebox-header" data-facebox-id="facebox-header">Users who have contributed to this file</h2>
      <ul class="facebox-user-list" data-facebox-id="facebox-description">
          <li class="facebox-user-list-item">
            <img alt="@aui" height="24" src="https://avatars2.githubusercontent.com/u/1791748?v=3&amp;s=48" width="24" />
            <a href="/aui">aui</a>
          </li>
      </ul>
    </div>
  </div>

<div class="file">
  <div class="file-header">
  <div class="file-actions">

    <div class="BtnGroup">
      <a href="/aui/art-template/raw/master/lib/template-web.js" class="btn btn-sm BtnGroup-item" id="raw-url">Raw</a>
        <a href="/aui/art-template/blame/master/lib/template-web.js" class="btn btn-sm js-update-url-with-hash BtnGroup-item" data-hotkey="b">Blame</a>
      <a href="/aui/art-template/commits/master/lib/template-web.js" class="btn btn-sm BtnGroup-item" rel="nofollow">History</a>
    </div>

        <a class="btn-octicon tooltipped tooltipped-nw"
           href="https://windows.github.com"
           aria-label="Open this file in GitHub Desktop"
           data-ga-click="Repository, open with desktop, type:windows">
            <svg aria-hidden="true" class="octicon octicon-device-desktop" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M15 2H1c-.55 0-1 .45-1 1v9c0 .55.45 1 1 1h5.34c-.25.61-.86 1.39-2.34 2h8c-1.48-.61-2.09-1.39-2.34-2H15c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm0 9H1V3h14v8z"/></svg>
        </a>

        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/aui/art-template/edit/master/lib/template-web.js" class="inline-form js-update-url-with-hash" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="EZjE1b420YQX2VqXebOIDGE6CF7b6PXCHNWb4PnXzgsf2flA5XSjdMs1YDmyuVdHAO8nTfb8HkudS2vwdNPvHA==" /></div>
          <button class="btn-octicon tooltipped tooltipped-nw" type="submit"
            aria-label="Fork this project and edit the file" data-hotkey="e" data-disable-with>
            <svg aria-hidden="true" class="octicon octicon-pencil" height="16" version="1.1" viewBox="0 0 14 16" width="14"><path fill-rule="evenodd" d="M0 12v3h3l8-8-3-3-8 8zm3 2H1v-2h1v1h1v1zm10.3-9.3L12 6 9 3l1.3-1.3a.996.996 0 0 1 1.41 0l1.59 1.59c.39.39.39 1.02 0 1.41z"/></svg>
          </button>
</form>        <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="/aui/art-template/delete/master/lib/template-web.js" class="inline-form" method="post"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /><input name="authenticity_token" type="hidden" value="okiFPxe/OnNgIEX+CkcFTxVwNxPeldJE/YA3Z5WgCg780HQkn548qWwF8kpQDyZUTpfX2sKiR9YjzK4sTZgkvQ==" /></div>
          <button class="btn-octicon btn-octicon-danger tooltipped tooltipped-nw" type="submit"
            aria-label="Fork this project and delete the file" data-disable-with>
            <svg aria-hidden="true" class="octicon octicon-trashcan" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M11 2H9c0-.55-.45-1-1-1H5c-.55 0-1 .45-1 1H2c-.55 0-1 .45-1 1v1c0 .55.45 1 1 1v9c0 .55.45 1 1 1h7c.55 0 1-.45 1-1V5c.55 0 1-.45 1-1V3c0-.55-.45-1-1-1zm-1 12H3V5h1v8h1V5h1v8h1V5h1v8h1V5h1v9zm1-10H2V3h9v1z"/></svg>
          </button>
</form>  </div>

  <div class="file-info">
      3 lines (3 sloc)
      <span class="file-info-divider"></span>
    14.7 KB
  </div>
</div>

  

  <div itemprop="text" class="blob-wrapper data type-javascript">
      <table class="highlight tab-size js-file-line-container" data-tab-size="8">
      <tr>
        <td id="L1" class="blob-num js-line-number" data-line-number="1"></td>
        <td id="LC1" class="blob-code blob-code-inner js-file-line">/*! art-template@4.8.1 for browser | https://github.com/aui/art-template */</td>
      </tr>
      <tr>
        <td id="L2" class="blob-num js-line-number" data-line-number="2"></td>
        <td id="LC2" class="blob-code blob-code-inner js-file-line">!function(e,t){&quot;object&quot;==typeof exports&amp;&amp;&quot;object&quot;==typeof module?module.exports=t():&quot;function&quot;==typeof define&amp;&amp;define.amd?define([],t):&quot;object&quot;==typeof exports?exports.template=t():e.template=t()}(this,function(){return function(e){function t(r){if(n[r])return n[r].exports;var i=n[r]={i:r,l:!1,exports:{}};return e[r].call(i.exports,i,i.exports,t),i.l=!0,i.exports}var n={};return t.m=e,t.c=n,t.i=function(e){return e},t.d=function(e,n,r){t.o(e,n)||Object.defineProperty(e,n,{configurable:!1,enumerable:!0,get:r})},t.n=function(e){var n=e&amp;&amp;e.__esModule?function(){return e[&quot;default&quot;]}:function(){return e};return t.d(n,&quot;a&quot;,n),n},t.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},t.p=&quot;&quot;,t(t.s=23)}([function(e,t,n){(function(t){e.exports=!1;try{e.exports=&quot;[object process]&quot;===Object.prototype.toString.call(t.process)}catch(n){}}).call(t,n(4))},function(e,t,n){&quot;use strict&quot;;var r=n(18),i=n(2),o=n(21),s=function(e,t){t.onerror(e,t);var n=function(){return&quot;{Template Error}&quot;};return n.mappings=[],n.sourcesContent=[],n},a=function c(e){var t=arguments.length&gt;1&amp;&amp;arguments[1]!==undefined?arguments[1]:{};&quot;string&quot;!=typeof e?t=e:t.source=e,t=i.$extend(t),e=t.source,t.debug&amp;&amp;(t.cache=!1,t.bail=!1,t.minimize=!1,t.compileDebug=!0),t.filename&amp;&amp;(t.filename=t.resolveFilename(t.filename,t));var n=t.filename,a=t.cache,u=t.caches;if(a&amp;&amp;n){var p=u.get(n);if(p)return p}if(!e)try{e=t.loader(n,t),t.source=e}catch(d){var l=new o({name:&quot;CompileError&quot;,message:&quot;template not found: &quot;+d.message,stack:d.stack});if(t.bail)throw l;return s(l,t)}var f=void 0,h=new r(t);try{f=h.build()}catch(l){if(l=new o(l),t.bail)throw l;return s(l,t)}var m=function(e,n){try{return f(e,n)}catch(l){if(!t.compileDebug)return t.cache=!1,t.compileDebug=!0,c(t)(e,n);if(l=new o(l),t.bail)throw l;return s(l,t)()}};return m.mappings=f.mappings,m.toString=function(){return f.toString()},a&amp;&amp;n&amp;&amp;u.set(n,m),m};a.Compiler=r,e.exports=a},function(e,t,n){&quot;use strict&quot;;var r=n(0),i=n(20),o=n(10),s=n(14),a=n(7),c=n(13),u=n(12),p=n(16),l=n(17),f=n(11),h=n(15),m={source:null,filename:null,rules:[l,p],escape:!0,debug:!!r&amp;&amp;&quot;production&quot;!==process.env.NODE_ENV,bail:!1,cache:!0,minimize:!0,compileDebug:!1,resolveFilename:h,htmlMinifier:f,htmlMinifierOptions:{collapseWhitespace:!0,minifyCSS:!0,minifyJS:!0,ignoreCustomFragments:[]},onerror:s,loader:c,caches:a,root:&quot;/&quot;,extname:&quot;.art&quot;,ignore:[],imports:i};m.$extend=function(){var e=arguments.length&gt;0&amp;&amp;arguments[0]!==undefined?arguments[0]:{};return o(e,m)},i.$include=u,e.exports=m},function(e,t){},function(e,t){var n;n=function(){return this}();try{n=n||Function(&quot;return this&quot;)()||(0,eval)(&quot;this&quot;)}catch(r){&quot;object&quot;==typeof window&amp;&amp;(n=window)}e.exports=n},function(e,t,n){&quot;use strict&quot;;e.exports=n(2)},function(e,t,n){&quot;use strict&quot;;var r=n(1),i=function(e,t,n){return r(e,n)(t)};e.exports=i},function(e,t,n){&quot;use strict&quot;;var r={__data:Object.create(null),set:function(e,t){this.__data[e]=t},get:function(e){return this.__data[e]},reset:function(){this.__data={}}};e.exports=r},function(e,t,n){&quot;use strict&quot;;var r=function(e,t){if(Array.isArray(e))for(var n=0,r=e.length;n&lt;r;n++)t(e[n],n,e);else for(var i in e)t(e[i],i)};e.exports=r},function(e,t,n){&quot;use strict&quot;;var r=function a(e){return&quot;string&quot;!=typeof e&amp;&amp;(e=e===undefined||null===e?&quot;&quot;:&quot;function&quot;==typeof e?a(e.call(e)):JSON.stringify(e)),e},i=/[&quot;&amp;&#39;&lt;&gt;]/,o=function(e){var t=&quot;&quot;+e,n=i.exec(t);if(!n)return e;var r=&quot;&quot;,o=void 0,s=void 0,a=void 0;for(o=n.index,s=0;o&lt;t.length;o++){switch(t.charCodeAt(o)){case 34:a=&quot;&amp;#34;&quot;;break;case 38:a=&quot;&amp;#38;&quot;;break;case 39:a=&quot;&amp;#39;&quot;;break;case 60:a=&quot;&amp;#60;&quot;;break;case 62:a=&quot;&amp;#62;&quot;;break;default:continue}s!==o&amp;&amp;(r+=t.substring(s,o)),s=o+1,r+=a}return s!==o?r+t.substring(s,o):r},s=function(e){return o(r(e))};e.exports=s},function(e,t,n){&quot;use strict&quot;;var r=Object.prototype.toString,i=function(e){return null===e?&quot;Null&quot;:r.call(e).slice(8,-1)},o=function s(e,t){var n=void 0,r=i(e);if(&quot;Object&quot;===r?n=Object.create(t||{}):&quot;Array&quot;===r&amp;&amp;(n=[].concat(t||[])),n){for(var o in e)e.hasOwnProperty(o)&amp;&amp;(n[o]=s(e[o],n[o]));return n}return e};e.exports=o},function(e,t,n){&quot;use strict&quot;;var r=n(0),i=function(e,t){if(r){var i,o=n(24).minify,s=t.htmlMinifierOptions,a=t.rules.map(function(e){return e.test});(i=s.ignoreCustomFragments).push.apply(i,a),e=o(e,s)}return e};e.exports=i},function(e,t,n){&quot;use strict&quot;;var r=function(e,t,r,i){var o=n(1);return i=i.$extend({filename:i.resolveFilename(e,i),source:null}),o(i)(t,r)};e.exports=r},function(e,t,n){&quot;use strict&quot;;var r=n(0),i=function(e){if(r){return n(3).readFileSync(e,&quot;utf8&quot;)}var t=document.getElementById(e);return t.value||t.innerHTML};e.exports=i},function(e,t,n){&quot;use strict&quot;;var r=function(e){console.error(e.name,e.message)};e.exports=r},function(e,t,n){&quot;use strict&quot;;var r=n(0),i=/^\.+\//,o=function(e,t){if(r){var o=n(3),s=t.root,a=t.extname;if(i.test(e)){var c=t.filename,u=!c||e===c,p=u?s:o.dirname(c);e=o.resolve(p,e)}else e=o.resolve(s,e);o.extname(e)||(e+=a)}return e};e.exports=o},function(e,t,n){&quot;use strict&quot;;var r={test:/{{[ \t]*([@#]?)(\/?)([\w\W]*?)[ \t]*}}/,use:function(e,t,n,i){var o=this,s=o.options,a=o.getEsTokens(i.trim()),c=a.map(function(e){return e.value}),u={},p=void 0,l=!!t&amp;&amp;&quot;raw&quot;,f=n+c.shift(),h=function(e,t){console.warn(&quot;Template upgrade:&quot;,&quot;{{&quot;+e+&quot;}}&quot;,&quot;&gt;&gt;&gt;&quot;,&quot;{{&quot;+t+&quot;}}&quot;,&quot;\n&quot;,s.filename||&quot;&quot;)};switch(&quot;#&quot;===t&amp;&amp;h(&quot;#value&quot;,&quot;@value&quot;),f){case&quot;set&quot;:i=&quot;var &quot;+c.join(&quot;&quot;);break;case&quot;if&quot;:i=&quot;if(&quot;+c.join(&quot;&quot;)+&quot;){&quot;;break;case&quot;else&quot;:var m=c.indexOf(&quot;if&quot;);m&gt;-1?(c.splice(0,m+1),i=&quot;}else if(&quot;+c.join(&quot;&quot;)+&quot;){&quot;):i=&quot;}else{&quot;;break;case&quot;/if&quot;:i=&quot;}&quot;;break;case&quot;each&quot;:p=r._split(a),p.shift(),&quot;as&quot;===p[1]&amp;&amp;(h(&quot;each object as value index&quot;,&quot;each object value index&quot;),p.splice(1,1));var d=p[0]||&quot;$data&quot;,v=p[1]||&quot;$value&quot;,g=p[2]||&quot;$index&quot;;i=&quot;$each(&quot;+d+&quot;,function(&quot;+v+&quot;,&quot;+g+&quot;){&quot;;break;case&quot;/each&quot;:i=&quot;})&quot;;break;case&quot;echo&quot;:f=&quot;print&quot;,h(&quot;echo value&quot;,&quot;value&quot;);case&quot;print&quot;:case&quot;include&quot;:case&quot;extend&quot;:p=r._split(a),p.shift(),i=f+&quot;(&quot;+p.join(&quot;,&quot;)+&quot;)&quot;;break;case&quot;block&quot;:i=&quot;block(&quot;+c.join(&quot;&quot;)+&quot;,function(){&quot;;break;case&quot;/block&quot;:i=&quot;})&quot;;break;default:if(-1!==c.indexOf(&quot;|&quot;)){for(var y=f,x=[],b=c.filter(function(e){return!/^\s+$/.test(e)});&quot;|&quot;!==b[0];)y+=b.shift();b.filter(function(e){return&quot;:&quot;!==e}).forEach(function(e){&quot;|&quot;===e?x.push([]):x[x.length-1].push(e)}),x.reduce(function(e,t){var n=t.shift();return t.unshift(e),i=&quot;$imports.&quot;+n+&quot;(&quot;+t.join(&quot;,&quot;)+&quot;)&quot;},y)}else s.imports[f]?(h(&quot;filterName value&quot;,&quot;value | filterName&quot;),p=r._split(a),p.shift(),i=f+&quot;(&quot;+p.join(&quot;,&quot;)+&quot;)&quot;,l=&quot;raw&quot;):i=&quot;&quot;+f+c.join(&quot;&quot;);l||(l=&quot;escape&quot;)}return u.code=i,u.output=l,u},_split:function(e){for(var t=0,n=e.shift(),r=[[n]];t&lt;e.length;){var i=e[t],o=i.type;&quot;whitespace&quot;!==o&amp;&amp;&quot;comment&quot;!==o&amp;&amp;(&quot;punctuator&quot;===n.type&amp;&amp;&quot;]&quot;!==n.value||&quot;punctuator&quot;===o?r[r.length-1].push(i):r.push([i]),n=i),t++}return r.map(function(e){return e.map(function(e){return e.value}).join(&quot;&quot;)})}};e.exports=r},function(e,t,n){&quot;use strict&quot;;var r={test:/&lt;%(#?)((?:==|=#|[=-])?)([\w\W]*?)(-?)%&gt;/,use:function(e,t,n,r){return n={&quot;-&quot;:&quot;raw&quot;,&quot;=&quot;:&quot;escape&quot;,&quot;&quot;:!1,&quot;==&quot;:&quot;raw&quot;,&quot;=#&quot;:&quot;raw&quot;}[n],t&amp;&amp;(r=&quot;/*&quot;+e+&quot;*/&quot;,n=!1),{code:r,output:n}}};e.exports=r},function(e,t,n){&quot;use strict&quot;;function r(e,t){if(!(e instanceof t))throw new TypeError(&quot;Cannot call a class as a function&quot;)}var i=n(19),o=n(22),s=&quot;$data&quot;,a=&quot;$imports&quot;,c=&quot;print&quot;,u=&quot;include&quot;,p=&quot;extend&quot;,l=&quot;block&quot;,f=&quot;$$out&quot;,h=&quot;$$line&quot;,m=&quot;$$blocks&quot;,d=&quot;$$from&quot;,v=&quot;$$layout&quot;,g=&quot;$$options&quot;,y=function(e,t){return e.hasOwnProperty(t)},x=JSON.stringify,b=function(){function e(t){var n,i,y,x=this;r(this,e);var b=t.source,k=t.minimize,w=t.htmlMinifier;if(this.options=t,this.stacks=[],this.context=[],this.scripts=[],this.CONTEXT_MAP={},this.external=(n={},n[s]=!0,n[a]=!0,n[g]=!0,n),this.internal=(i={},i[f]=&quot;&#39;&#39;&quot;,i[h]=&quot;[0,0,&#39;&#39;]&quot;,i[m]=&quot;arguments[1]||{}&quot;,i[d]=&quot;null&quot;,i[v]=&quot;function(){return &quot;+a+&quot;.$include(&quot;+d+&quot;,&quot;+s+&quot;,&quot;+m+&quot;,&quot;+g+&quot;)}&quot;,i[c]=&quot;function(){&quot;+f+&quot;+=&#39;&#39;.concat.apply(&#39;&#39;,arguments)}&quot;,i[u]=&quot;function(src,data,block){&quot;+f+&quot;+=&quot;+a+&quot;.$include(src,data||&quot;+s+&quot;,block,&quot;+g+&quot;)}&quot;,i[p]=&quot;function(from){&quot;+d+&quot;=from}&quot;,i[l]=&quot;function(name,callback){if(&quot;+d+&quot;){&quot;+f+&quot;=&#39;&#39;;callback();&quot;+m+&quot;[name]=&quot;+f+&quot;}else{if(typeof &quot;+m+&quot;[name]===&#39;string&#39;){&quot;+f+&quot;+=&quot;+m+&quot;[name]}else{callback()}}}&quot;,i),this.dependencies=(y={},y[c]=[f],y[u]=[f,a,s,g],y[p]=[d,v],y[l]=[d,f,m],y[v]=[a,d,s,m,g],y),this.importContext(f),t.compileDebug&amp;&amp;this.importContext(h),k)try{b=w(b,t)}catch(E){}this.source=b,this.getTplTokens(b,t.rules,this).forEach(function(e){e.type===o.TYPE_STRING?x.parseString(e):x.parseExpression(e)})}return e.prototype.getTplTokens=function(){return o.apply(undefined,arguments)},e.prototype.getEsTokens=function(e){return i(e)},e.prototype.getVariables=function(e){var t=!1;return e.filter(function(e){return&quot;whitespace&quot;!==e.type&amp;&amp;&quot;comment&quot;!==e.type}).filter(function(e){return&quot;name&quot;===e.type&amp;&amp;!t||(t=&quot;punctuator&quot;===e.type&amp;&amp;&quot;.&quot;===e.value,!1)}).map(function(e){return e.value})},e.prototype.importContext=function(e){var t=this,n=&quot;&quot;,r=this.internal,i=this.dependencies,o=this.external,c=this.context,u=this.options,p=u.ignore,l=u.imports,f=this.CONTEXT_MAP;!y(f,e)&amp;&amp;!y(o,e)&amp;&amp;p.indexOf(e)&lt;0&amp;&amp;(y(r,e)?(n=r[e],y(i,e)&amp;&amp;i[e].forEach(function(e){return t.importContext(e)})):n=y(l,e)?a+&quot;.&quot;+e:s+&quot;.&quot;+e,f[e]=n,c.push({name:e,value:n}))},e.prototype.parseString=function(e){var t=e.value;if(t){var n=f+&quot;+=&quot;+x(t);this.scripts.push({source:t,tplToken:e,code:n})}},e.prototype.parseExpression=function(e){var t=this,n=e.value,r=e.script,i=r.output,s=r.code;i&amp;&amp;(s=!1===escape||i===o.TYPE_RAW?f+&quot;+=&quot;+r.code:f+&quot;+=$escape(&quot;+r.code+&quot;)&quot;);var a=this.getEsTokens(s);this.getVariables(a).forEach(function(e){return t.importContext(e)}),this.scripts.push({source:n,tplToken:e,code:s})},e.prototype.checkExpression=function(e){for(var t=[[/^\s*}[\w\W]*?{?[\s;]*$/,&quot;&quot;],[/(^[\w\W]*?\s*function\s*\([\w\W]*?\)\s*{[\s;]*$)/,&quot;$1})&quot;],[/(^.*?\(\s*[\w\W]*?=&gt;\s*{[\s;]*$)/,&quot;$1})&quot;],[/(^[\w\W]*?\([\w\W]*?\)\s*{[\s;]*$)/,&quot;$1}&quot;]],n=0;n&lt;t.length;){if(t[n][0].test(e)){var r;e=(r=e).replace.apply(r,t[n]);break}n++}try{return new Function(e),!0}catch(i){return!1}},e.prototype.build=function(){var e=this.options,t=this.context,n=this.scripts,r=this.stacks,i=this.source,c=e.filename,u=e.imports,l=[],m=y(this.CONTEXT_MAP,p),d=0,b=function(e,t){var n=t.line,i=t.start,o={generated:{line:r.length+d+1,column:1},original:{line:n+1,column:i+1}};return d+=e.split(/\n/).length-1,o},k=function(e){return e.replace(/^[\t ]+|[\t ]$/g,&quot;&quot;)};r.push(&quot;function(&quot;+s+&quot;){&quot;),r.push(&quot;&#39;use strict&#39;&quot;),r.push(&quot;var &quot;+t.map(function(e){return e.name+&quot;=&quot;+e.value}).join(&quot;,&quot;)),e.compileDebug?(r.push(&quot;try{&quot;),n.forEach(function(e){e.tplToken.type===o.TYPE_EXPRESSION&amp;&amp;r.push(h+&quot;=[&quot;+[e.tplToken.line,e.tplToken.start,x(e.source)].join(&quot;,&quot;)+&quot;]&quot;),l.push(b(e.code,e.tplToken)),r.push(k(e.code))}),r.push(&quot;}catch(error){&quot;),r.push(&quot;throw {&quot;+[&quot;name:&#39;RuntimeError&#39;&quot;,&quot;path:&quot;+x(c),&quot;message:error.message&quot;,&quot;line:&quot;+h+&quot;[0]+1&quot;,&quot;column:&quot;+h+&quot;[1]+1&quot;,&quot;source:&quot;+h+&quot;[2]&quot;,&quot;stack:error.stack&quot;].join(&quot;,&quot;)+&quot;}&quot;),r.push(&quot;}&quot;)):n.forEach(function(e){l.push(b(e.code,e.tplToken)),r.push(k(e.code))}),r.push(m?&quot;return &quot;+v+&quot;()&quot;:&quot;return &quot;+f),r.push(&quot;}&quot;);var w=r.join(&quot;\n&quot;);try{var E=new Function(a,g,&quot;return &quot;+w)(u,e);return E.mappings=l,E.sourcesContent=[i],E}catch(C){for(var $=0,T=0,O=0,j=i;$&lt;n.length;){var S=n[$];if(!this.checkExpression(S.code)){j=S.source,T=S.tplToken.line,O=S.tplToken.start;break}$++}throw{name:&quot;CompileError&quot;,path:c,message:C.message,line:T+1,column:O+1,source:j,script:w,stack:C.stack}}},e}();b.CONSTS={DATA:s,IMPORTS:a,PRINT:c,INCLUDE:u,EXTEND:p,BLOCK:l,OPTIONS:g,OUT:f,LINE:h,BLOCKS:m,FROM:d,LAYOUT:v,ESCAPE:&quot;$escape&quot;},e.exports=b},function(e,t,n){&quot;use strict&quot;;var r=n(25),i=/(([&#39;&quot;])(?:(?!\2|\\).|\\(?:\r\n|[\s\S]))*(\2)?|`(?:[^`\\$]|\\[\s\S]|\$(?!\{)|\$\{(?:[^{}]|\{[^}]*\}?)*\}?)*(`)?)|(\/\/.*)|(\/\*(?:[^*]|\*(?!\/))*(\*\/)?)|(\/(?!\*)(?:\[(?:(?![\]\\]).|\\.)*\]|(?![\/\]\\]).|\\.)+\/(?:(?!\s*(?:\b|[\u0080-\uFFFF$\\&#39;&quot;~({]|[+\-!](?!=)|\.?\d))|[gmiyu]{1,5}\b(?![\u0080-\uFFFF$\\]|\s*(?:[+\-*%&amp;|^&lt;&gt;!=?({]|\/(?![\/*])))))|(0[xX][\da-fA-F]+|0[oO][0-7]+|0[bB][01]+|(?:\d*\.\d+|\d+\.?)(?:[eE][+-]?\d+)?)|((?!\d)(?:(?!\s)[$\w\u0080-\uFFFF]|\\u[\da-fA-F]{4}|\\u\{[\da-fA-F]+\})+)|(--|\+\+|&amp;&amp;|\|\||=&gt;|\.{3}|(?:[+\-\/%&amp;|^]|\*{1,2}|&lt;{1,2}|&gt;{1,3}|!=?|={1,2})=?|[?~.,:;[\](){}])|(\s+)|(^$|[\s\S])/g,o=function(e){var t={type:&quot;invalid&quot;,value:e[0]};return e[1]?(t.type=&quot;string&quot;,t.closed=!(!e[3]&amp;&amp;!e[4])):e[5]?t.type=&quot;comment&quot;:e[6]?(t.type=&quot;comment&quot;,t.closed=!!e[7]):e[8]?t.type=&quot;regex&quot;:e[9]?t.type=&quot;number&quot;:e[10]?t.type=&quot;name&quot;:e[11]?t.type=&quot;punctuator&quot;:e[12]&amp;&amp;(t.type=&quot;whitespace&quot;),t},s=function(e){return e.match(i).map(function(e){return i.lastIndex=0,o(i.exec(e))}).map(function(e){return&quot;name&quot;===e.type&amp;&amp;r(e.value)&amp;&amp;(e.type=&quot;keyword&quot;),e})};e.exports=s},function(e,t,n){&quot;use strict&quot;;(function(t){/*! art-template@runtime | https://github.com/aui/art-template */</td>
      </tr>
      <tr>
        <td id="L3" class="blob-num js-line-number" data-line-number="3"></td>
        <td id="LC3" class="blob-code blob-code-inner js-file-line">var r=n(0),i=n(8),o=n(9),s=Object.create(r?t:window);s.$each=i,s.$escape=o,e.exports=s}).call(t,n(4))},function(e,t,n){&quot;use strict&quot;;function r(e){var t=e.stack;delete e.stack,this.name=&quot;TemplateError&quot;,this.message=JSON.stringify(e,null,4),this.stack=t}r.prototype=Object.create(Error.prototype),r.prototype.constructor=r,e.exports=r},function(e,t,n){&quot;use strict&quot;;var r=function(e,t,n){for(var r=[{type:&quot;string&quot;,value:e,line:0,start:0,end:e.length}],i=0;i&lt;t.length;i++)!function(e){for(var t=e.test.ignoreCase?&quot;ig&quot;:&quot;g&quot;,i=e.test.source+&quot;|^$|[\\w\\W]&quot;,o=new RegExp(i,t),s=0;s&lt;r.length;s++)if(&quot;string&quot;===r[s].type){for(var a=r[s].line,c=r[s].start,u=r[s].end,p=r[s].value.match(o),l=[],f=0;f&lt;p.length;f++){var h=p[f];e.test.lastIndex=0;var m=e.test.exec(h),d=m?&quot;expression&quot;:&quot;string&quot;,v=l[l.length-1],g=v||r[s],y=g.value;c=g.line===a?v?v.end:c:y.length-y.lastIndexOf(&quot;\n&quot;)-1,u=c+h.length;var x={type:d,value:h,line:a,start:c,end:u};if(&quot;string&quot;===d)v&amp;&amp;&quot;string&quot;===v.type?(v.value+=h,v.end+=h.length):l.push(x);else{var b=e.use.apply(n,m);x.script=b,l.push(x)}a+=h.split(/\n/).length-1}r.splice.apply(r,[s,1].concat(l)),s+=l.length-1}}(t[i]);return r};r.TYPE_STRING=&quot;string&quot;,r.TYPE_EXPRESSION=&quot;expression&quot;,r.TYPE_RAW=&quot;raw&quot;,r.TYPE_ESCAPE=&quot;escape&quot;,e.exports=r},function(e,t,n){&quot;use strict&quot;;var r=n(6),i=n(1),o=n(5),s=function(e,t){return t instanceof Object?r({filename:e},t):i({filename:e,source:t})};s.render=r,s.compile=i,s.defaults=o,e.exports=s},function(e,t){!function(e){e.noop=function(){}}(&quot;object&quot;==typeof e&amp;&amp;&quot;object&quot;==typeof e.exports?e.exports:window)},function(e,t,n){&quot;use strict&quot;;var r={&quot;abstract&quot;:!0,await:!0,&quot;boolean&quot;:!0,&quot;break&quot;:!0,&quot;byte&quot;:!0,&quot;case&quot;:!0,&quot;catch&quot;:!0,&quot;char&quot;:!0,&quot;class&quot;:!0,&quot;const&quot;:!0,&quot;continue&quot;:!0,&quot;debugger&quot;:!0,&quot;default&quot;:!0,&quot;delete&quot;:!0,&quot;do&quot;:!0,&quot;double&quot;:!0,&quot;else&quot;:!0,&quot;enum&quot;:!0,&quot;export&quot;:!0,&quot;extends&quot;:!0,&quot;false&quot;:!0,&quot;final&quot;:!0,&quot;finally&quot;:!0,&quot;float&quot;:!0,&quot;for&quot;:!0,&quot;function&quot;:!0,&quot;goto&quot;:!0,&quot;if&quot;:!0,&quot;implements&quot;:!0,&quot;import&quot;:!0,&quot;in&quot;:!0,&quot;instanceof&quot;:!0,&quot;int&quot;:!0,&quot;interface&quot;:!0,&quot;let&quot;:!0,&quot;long&quot;:!0,&quot;native&quot;:!0,&quot;new&quot;:!0,&quot;null&quot;:!0,&quot;package&quot;:!0,&quot;private&quot;:!0,&quot;protected&quot;:!0,&quot;public&quot;:!0,&quot;return&quot;:!0,&quot;short&quot;:!0,&quot;static&quot;:!0,&quot;super&quot;:!0,&quot;switch&quot;:!0,&quot;synchronized&quot;:!0,&quot;this&quot;:!0,&quot;throw&quot;:!0,&quot;transient&quot;:!0,&quot;true&quot;:!0,&quot;try&quot;:!0,&quot;typeof&quot;:!0,&quot;var&quot;:!0,&quot;void&quot;:!0,&quot;volatile&quot;:!0,&quot;while&quot;:!0,&quot;with&quot;:!0,&quot;yield&quot;:!0};e.exports=function(e){return r.hasOwnProperty(e)}}])});</td>
      </tr>
</table>

  </div>

</div>

<button type="button" data-facebox="#jump-to-line" data-facebox-class="linejump" data-hotkey="l" class="d-none">Jump to Line</button>
<div id="jump-to-line" style="display:none">
  <!-- '"` --><!-- </textarea></xmp> --></option></form><form accept-charset="UTF-8" action="" class="js-jump-to-line-form" method="get"><div style="margin:0;padding:0;display:inline"><input name="utf8" type="hidden" value="&#x2713;" /></div>
    <input class="form-control linejump-input js-jump-to-line-field" type="text" placeholder="Jump to line&hellip;" aria-label="Jump to line" autofocus>
    <button type="submit" class="btn">Go</button>
</form></div>


  </div>
  <div class="modal-backdrop js-touch-events"></div>
</div>

    </div>
  </div>

  </div>

      <div class="container site-footer-container">
  <div class="site-footer" role="contentinfo">
    <ul class="site-footer-links float-right">
        <li><a href="https://github.com/contact" data-ga-click="Footer, go to contact, text:contact">Contact GitHub</a></li>
      <li><a href="https://developer.github.com" data-ga-click="Footer, go to api, text:api">API</a></li>
      <li><a href="https://training.github.com" data-ga-click="Footer, go to training, text:training">Training</a></li>
      <li><a href="https://shop.github.com" data-ga-click="Footer, go to shop, text:shop">Shop</a></li>
        <li><a href="https://github.com/blog" data-ga-click="Footer, go to blog, text:blog">Blog</a></li>
        <li><a href="https://github.com/about" data-ga-click="Footer, go to about, text:about">About</a></li>

    </ul>

    <a href="https://github.com" aria-label="Homepage" class="site-footer-mark" title="GitHub">
      <svg aria-hidden="true" class="octicon octicon-mark-github" height="24" version="1.1" viewBox="0 0 16 16" width="24"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"/></svg>
</a>
    <ul class="site-footer-links">
      <li>&copy; 2017 <span title="0.20049s from github-fe-612ff0a.cp1-iad.github.net">GitHub</span>, Inc.</li>
        <li><a href="https://github.com/site/terms" data-ga-click="Footer, go to terms, text:terms">Terms</a></li>
        <li><a href="https://github.com/site/privacy" data-ga-click="Footer, go to privacy, text:privacy">Privacy</a></li>
        <li><a href="https://github.com/security" data-ga-click="Footer, go to security, text:security">Security</a></li>
        <li><a href="https://status.github.com/" data-ga-click="Footer, go to status, text:status">Status</a></li>
        <li><a href="https://help.github.com" data-ga-click="Footer, go to help, text:help">Help</a></li>
    </ul>
  </div>
</div>



  

  <div id="ajax-error-message" class="ajax-error-message flash flash-error">
    <svg aria-hidden="true" class="octicon octicon-alert" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.865 1.52c-.18-.31-.51-.5-.87-.5s-.69.19-.87.5L.275 13.5c-.18.31-.18.69 0 1 .19.31.52.5.87.5h13.7c.36 0 .69-.19.86-.5.17-.31.18-.69.01-1L8.865 1.52zM8.995 13h-2v-2h2v2zm0-3h-2V6h2v4z"/></svg>
    <button type="button" class="flash-close js-flash-close js-ajax-error-dismiss" aria-label="Dismiss error">
      <svg aria-hidden="true" class="octicon octicon-x" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
    </button>
    You can't perform that action at this time.
  </div>


    
    <script crossorigin="anonymous" integrity="sha256-5U4AYElhDMKX/hM/gkyHhMcS4XDLjAyAKu0qdnsoV+Y=" src="https://assets-cdn.github.com/assets/frameworks-e54e006049610cc297fe133f824c8784c712e170cb8c0c802aed2a767b2857e6.js"></script>
    <script async="async" crossorigin="anonymous" integrity="sha256-t8/9nXUQEk1+Ne7xa4x4Hl9xOjNEJvmfKHpZ0ddmXRI=" src="https://assets-cdn.github.com/assets/github-b7cffd9d7510124d7e35eef16b8c781e5f713a334426f99f287a59d1d7665d12.js"></script>
    
    
    
    
  <div class="js-stale-session-flash stale-session-flash flash flash-warn flash-banner d-none">
    <svg aria-hidden="true" class="octicon octicon-alert" height="16" version="1.1" viewBox="0 0 16 16" width="16"><path fill-rule="evenodd" d="M8.865 1.52c-.18-.31-.51-.5-.87-.5s-.69.19-.87.5L.275 13.5c-.18.31-.18.69 0 1 .19.31.52.5.87.5h13.7c.36 0 .69-.19.86-.5.17-.31.18-.69.01-1L8.865 1.52zM8.995 13h-2v-2h2v2zm0-3h-2V6h2v4z"/></svg>
    <span class="signed-in-tab-flash">You signed in with another tab or window. <a href="">Reload</a> to refresh your session.</span>
    <span class="signed-out-tab-flash">You signed out in another tab or window. <a href="">Reload</a> to refresh your session.</span>
  </div>
  <div class="facebox" id="facebox" style="display:none;">
  <div class="facebox-popup">
    <div class="facebox-content" role="dialog" aria-labelledby="facebox-header" aria-describedby="facebox-description">
    </div>
    <button type="button" class="facebox-close js-facebox-close" aria-label="Close modal">
      <svg aria-hidden="true" class="octicon octicon-x" height="16" version="1.1" viewBox="0 0 12 16" width="12"><path fill-rule="evenodd" d="M7.48 8l3.75 3.75-1.48 1.48L6 9.48l-3.75 3.75-1.48-1.48L4.52 8 .77 4.25l1.48-1.48L6 6.52l3.75-3.75 1.48 1.48z"/></svg>
    </button>
  </div>
</div>


  </body>
</html>

